import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.*;
/**
 * References:
 * https://stackoverflow.com/questions/55389052/how-to-set-javafx-textfield-to-wrap-text-inside-field
 * 
 */

/**
 * A Recipe in RecipeList (App display)
 */
class RecipeBox extends HBox{
    private Button title;
    private String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    
    RecipeBox(String title) {
        this.setPrefSize(500, 50);

        // Set recipe appearance 
        this.title = new Button();
        this.title.setPrefSize(500, 50);
        this.title.setText(title);
        this.title.setStyle(defaultButtonStyle);
        this.getChildren().add(this.title);
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
    public void addRecipe(String title){
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
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addRecipeButton = new Button("Add Recipe");
        addRecipeButton.setStyle(defaultButtonStyle);

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

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(defaultButtonStyle);

        this.getChildren().add(titleText);
        this.getChildren().add(returnButton);
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
    private Label descriptionLabel;
    private TextArea description;

    AddWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        titleLabel = new Label();
        titleLabel.setText("Title");
        titleLabel.setPrefSize(100, 30);
        
        title = new TextField();
        title.setPrefSize(400,50);

        descriptionLabel = new Label();
        descriptionLabel.setText("Description");

        description = new TextArea();
        description.setPrefSize(200,300);

        this.getChildren().addAll(titleLabel, title, descriptionLabel, description);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getDescription() {
        return description.getText();
    }

    public void clear() {
        title.clear();
        description.clear();
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
class AddWindow extends BorderPane{
    private AddWindowHeader header;
    private AddWindowBody body;
    private AddWindowFooter footer;

    AddWindow(){
        header =  new AddWindowHeader();
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

public class PantryPal extends Application {
    public static List<Recipe> recipeStorage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initiate recipe storage
        recipeStorage = new ArrayList<>();

        // Setting the layout of the MainWindow
        MainWindow mainWindow = new MainWindow();
        Scene mainScene = new Scene(mainWindow, 500, 400);

        // Setting the layout of the AddWindow
        AddWindow addWindow = new AddWindow();
        Scene addScene = new Scene(addWindow, 500,400);

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(mainScene);

        // Link addRecipeButton with its function
        Button addRecipeButton = mainWindow.getHeader().getAddButton();
        addRecipeButton.setOnAction(e -> primaryStage.setScene(addScene));

        // Link returnButton with its function
        Button returnButton = addWindow.getAddWindowHeader().getReturnButton();
        AddWindowBody addWindowBody = addWindow.getAddWindowBody();
        returnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            addWindowBody.clear();
            primaryStage.setScene(mainScene);
        });

        // Link completeButton with its function
        Button completeButton = addWindow.getAddWindowFooter().getCompleteButton();
        completeButton.setOnAction(e -> {
            String title = addWindowBody.getTitle();
            String description = addWindowBody.getDescription();
            Recipe newRecipe = new Recipe(title, description);
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