package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.IOException;
import java.util.*;

/**
 * References:
 * https://stackoverflow.com/questions/55389052/how-to-set-javafx-textfield-to-wrap-text-inside-field
 * 
 */

/**
 * A Recipe in RecipeList (App display)
 */
class RecipeBox extends HBox {
    private Button title;
    private String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    private Button deleteButton;

    RecipeBox(String title) {
        this.setPrefSize(500, 50);

        // Set recipe appearance
        this.title = new Button();
        this.title.setPrefSize(500, 50);
        this.title.setText(title);
        this.title.setStyle(defaultButtonStyle);
        this.getChildren().add(this.title);

        //Set up delete button apperance
        this.deleteButton = new Button("delete");
        this.deleteButton.setPrefSize(150, 100);

        //deletes the recipe from the list
        this.deleteButton.setOnAction(e -> {
            Recipe Rp = getRecipeByTitle(this.title.getText());
            PantryPal.recipeStorage.remove(Rp);
            try {
                RecipeList List = (RecipeList) getParent();
                List.getChildren().remove(this);
            } catch (NullPointerException error) {
            }
            ;
        });
        //adds the delete button
        this.getChildren().add(this.deleteButton);

        // Creates a new RecipeDetailsView window using selected recipe
        this.title.setOnAction(e -> {
            Recipe recipe = getRecipeByTitle(title);
            if (recipe != null) {
                RecipeDetailsView recipeDetailsView = new RecipeDetailsView(recipe, this.getScene());
                Scene recipeDetailsScene = new Scene(recipeDetailsView, 500, 400);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setScene(recipeDetailsScene);
            }
        });
    }

    private Recipe getRecipeByTitle(String title) {
        for (Recipe recipe : PantryPal.recipeStorage) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }
        return null;
    }
}

/**
 * The main recipe list displayed in app.
 */
class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    // Adds a single recipe to the main list given the title of recipe
    public void addRecipe(String title) {
        this.getChildren().add(0, new RecipeBox(title)); // add new recipe to top of list
    }
}

/**
 * Header segment of main window, displays Recipe List and the AddRecipe button
 */
class MainWindowHeader extends HBox {

    private Button addRecipeButton;

    MainWindowHeader() {
        // Set Header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        // Add Recipe button
        // String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addRecipeButton = new Button("Add Recipe");
        addRecipeButton.setStyle(getAccessibleHelp());;

        this.getChildren().addAll(titleText, addRecipeButton);
    }

    public Button getAddButton() {
        return addRecipeButton;
    }

}

/**
 * Main window layout
 */
class MainWindow extends BorderPane {
    private MainWindowHeader mainWindowHeader; // header section
    private RecipeList recipeList;
    private ScrollPane scrollPane;

    MainWindow() {
        mainWindowHeader = new MainWindowHeader();
        recipeList = new RecipeList();

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(mainWindowHeader);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollPane);
    }

    public MainWindowHeader getHeader() {
        return mainWindowHeader;
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }
}

/**
 * Header segment for AddWindow
 */
class AddWindowHeader extends HBox {
    private Button returnButton;

    AddWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Add Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        //String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(getAccessibleHelp());

        this.getChildren().addAll(titleText, returnButton);
    }

    public Button getReturnButton() {
        return returnButton;
    }
}

/**
 * Center segment for AddWindow
 */
class AddWindowBody extends VBox {
    private Label titleLabel;
    private TextField title;
    private Label mealLabel;
    private TextField mealType;
    private Label descriptionLabel;
    private TextArea description;

    AddWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        titleLabel = new Label();
        titleLabel.setText("Title");
        titleLabel.setPrefSize(100, 30);

        title = new TextField();
        title.setPrefSize(400, 50);

        mealLabel = new Label();
        mealLabel.setText("Meal Type");
        mealLabel.setPrefSize(100, 30);

        mealType = new TextField();
        mealType.setPrefSize(400, 50);

        descriptionLabel = new Label();
        descriptionLabel.setText("Description");

        description = new TextArea();
        description.setPrefSize(200, 300);

        this.getChildren().addAll(titleLabel, title, mealLabel, mealType, descriptionLabel, description);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getMealType() {
        return mealType.getText();
    }

    public String getDescription() {
        return description.getText();
    }

    public void clear() {
        title.clear();
        description.clear();
    }

    public void setRecipe(String suggestedrecipe){
        int recipeTitleIdx = suggestedrecipe.indexOf("Recipe Title:");
        int mealTypeIdx = suggestedrecipe.indexOf("Meal Type:");
        int recipeInstructionsIdx = suggestedrecipe.indexOf("Recipe Instructions:");
        String parseTitle = suggestedrecipe.substring(recipeTitleIdx + 14, mealTypeIdx).trim();
        String parseMealType = suggestedrecipe.substring(mealTypeIdx + 11, recipeInstructionsIdx).trim();
        String parseInstruction = suggestedrecipe.substring(recipeInstructionsIdx + 21).trim();
        title.setText(parseTitle);
        mealType.setText(parseMealType);
        description.setText(parseInstruction);
    }
}




/**
 * Footer segment for AddWindow
 */
class AddWindowFooter extends HBox {
    private Button completeButton;

    AddWindowFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        completeButton = new Button();
        completeButton.setText("Complete");

        this.getChildren().addAll(completeButton);
    }

    public Button getCompleteButton() {
        return completeButton;
    }
}

/**
 * Add window layout
 */
class AddWindow extends BorderPane {
    private AddWindowHeader header;
    private AddWindowBody body;
    private AddWindowFooter footer;

    AddWindow() {
        header = new AddWindowHeader();
        body = new AddWindowBody();
        footer = new AddWindowFooter();

        this.setTop(header);
        this.setCenter(body);
        this.setBottom(footer);
    }

    public AddWindowHeader getAddWindowHeader() {
        return header;
    }

    public AddWindowBody getAddWindowBody() {
        return body;
    }

    public AddWindowFooter getAddWindowFooter() {
        return footer;
    }
}

class PromptWindow extends BorderPane {
    private PromptWindowHeader header;
    private PromptWindowBody body;


    PromptWindow() {
        header = new PromptWindowHeader();
        body = new PromptWindowBody();


        this.setTop(header);
        this.setCenter(body);

    }

    public PromptWindowHeader getPromptWindowHeader() {
        return header;
    }

    public PromptWindowBody getPromptWindowBody() {
        return body;
    }
}

class PromptWindowHeader extends HBox {
    private Button returnButton;
    private ComboBox<String> mealTypeDropMenu;

    PromptWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Suggest "); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center
        Label prompt = new Label("include meal type and ingredients");
        
        //String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(getAccessibleHelp());

        String meal_types[] = {"Breakfast", "Lunch", "Dinner"};
        mealTypeDropMenu = new ComboBox<>(FXCollections.observableArrayList(meal_types));
        mealTypeDropMenu.setStyle(getAccessibleHelp());
        mealTypeDropMenu.setPromptText("Select meal type");
        this.getChildren().addAll(titleText,prompt, returnButton, mealTypeDropMenu);
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public String getMealType() {
        return mealTypeDropMenu.getValue();
    }
}


class PromptWindowBody extends VBox {
    private Label ingredientLabel;
    private TextArea description;
    private Button suggesButton;
    PromptWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");
        

        ingredientLabel = new Label();
        ingredientLabel.setText("Ingredient");
        ingredientLabel.setPrefSize(100,30);
        ingredientLabel.setPadding(new Insets(20,0,20,20));
        description = new TextArea();
        description.setPrefSize(200, 300);

        suggesButton = new Button("generate recipe");
        suggesButton.setStyle(getAccessibleHelp());
        suggesButton.setPrefSize(100, 30);
        this.getChildren().addAll(ingredientLabel, description, suggesButton);
    }

   

    public String getDescription() {
        return description.getText();
    }
    public Button getSuggestButton(){
        return suggesButton;
    }
    public void clear() {
        description.clear();
    }

    
}

/**
 * Footer segment for RecipeViewWindow
 */
class RecipeViewFooter extends HBox {
    private Button saveButton;
    private Button returnButton;

    RecipeViewFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        saveButton = new Button();
        saveButton.setText("Save");

        returnButton = new Button();
        returnButton.setText("Return");

        this.getChildren().addAll(saveButton, returnButton);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

}

/**
 * Recipe details window layout
 */
class RecipeDetailsView extends BorderPane {
    private TextArea descriptionTextArea;
    private RecipeViewFooter footer;

    RecipeDetailsView(Recipe recipe, Scene previousScene) {
        footer = new RecipeViewFooter();
        this.setStyle("-fx-background-color: #F0F8FF;");

        String recipeTitle = recipe.getTitle();
        Text titleText = new Text(recipeTitle);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        String recipeDescription = recipe.getDescription();
        descriptionTextArea = new TextArea(recipeDescription);
        descriptionTextArea.setPrefSize(400, 300);

        // Return button retrieves current stage and returns to the previous scene
        // (main)
        footer.getReturnButton().setOnAction(e -> {
            Stage stage = (Stage) footer.getReturnButton().getScene().getWindow();
            stage.setScene(previousScene);
        });

        this.setTop(titleText);
        this.setCenter(descriptionTextArea);
        this.setBottom(footer);

        footer.getSaveButton().setOnAction(e -> {
            recipe.setDescription(this.getDescription());
        });
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }
}

public class PantryPal extends Application {
    public static List<Recipe> recipeStorage;
    public String suggestedrecipe;
    @Override
    public void start(Stage primaryStage)  throws Exception,IOException, InterruptedException{
        // Initiate recipe storage
        recipeStorage = new ArrayList<>();

        // Setting the layout of the MainWindow
        MainWindow mainWindow = new MainWindow();
        Scene mainScene = new Scene(mainWindow, 600, 500);
        
        // Setting the layout of the AddWindow
        PromptWindow promptWindow = new PromptWindow();
        Scene promptScene = new Scene(promptWindow, 600, 400);

        AddWindow addWindow = new AddWindow();
        Scene addScene =  new Scene(addWindow, 600,400);
        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(mainScene);

        // Link addRecipeButton with its function
        Button addRecipeButton = mainWindow.getHeader().getAddButton();
        addRecipeButton.setOnAction(e -> primaryStage.setScene(promptScene));

        // generate recipe button
        Button suggestButton = promptWindow.getPromptWindowBody().getSuggestButton();
        suggestButton.setOnAction(e -> {
            try{
                ChatAPI instruction = new ChatAPI(promptWindow.getPromptWindowBody().getDescription());
                suggestedrecipe = instruction.suggestRecipe();
                primaryStage.setScene(addScene);
                promptWindow.getPromptWindowBody().clear();
                addWindow.getAddWindowBody().setRecipe(suggestedrecipe);
            }catch(Exception ex){

            }
        });

        // Link returnButton with its function
        Button suggestReturnButton = promptWindow.getPromptWindowHeader().getReturnButton();
        PromptWindowBody promptWindowBody = promptWindow.getPromptWindowBody();
        suggestReturnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            promptWindowBody.clear();
            primaryStage.setScene(mainScene);
        });
        

        // Link returnButton with its function
        Button addReturnButton = addWindow.getAddWindowHeader().getReturnButton();
        AddWindowHeader addWindowHeader = addWindow.getAddWindowHeader();
        AddWindowBody addWindowBody = addWindow.getAddWindowBody();
        addReturnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            addWindowBody.clear();
            primaryStage.setScene(mainScene);
        });

        // Link completeButton with its function
        Button completeButton = addWindow.getAddWindowFooter().getCompleteButton();
        completeButton.setOnAction(e -> {
            String title = addWindowBody.getTitle();
            String description = addWindowBody.getDescription();
            String mealType = addWindowBody.getMealType();
            Recipe newRecipe = new Recipe(title, description, mealType);
            RecipeList recipeList = mainWindow.getRecipeList();

            // Store recipe in storage for view/delete/edit
            recipeStorage.add(newRecipe);
            recipeList.addRecipe(title);

            // Clear text in addWindow
            addWindowBody.clear();

            // Switch back to mainScene
            primaryStage.setScene(mainScene);
        });

        // Make window non-resizable
        primaryStage.setResizable(false);

        // Show the app
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}