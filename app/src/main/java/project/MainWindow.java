package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

/**
 * Header segment of main window, displays Recipe List and the AddRecipe button
 */
class MainWindowHeader extends HBox {

    private Button addRecipeButton;
    private Button restoreRecipes;
    private ComboBox<Button> SortBox;// create a dropdown bar for sorting
    private Button alphabetButton = new Button("alphabetically"); // create button for sorting alphabetically
    private Button oldtoNewButton = new Button("chronologically from oldest to newest"); // create button for sorting from new to old
    private Button newtoOldButton = new Button ("Default (newest to oldese)"); // create button for sorting from old to new


    MainWindowHeader() {
        // Set Header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        // Set Style to Sort Option
        alphabetButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 0;");
        // Link sortfunction to alphabetButton
        alphabetButton.setOnAction(e1 -> {
            ArrayList<Recipe> list = new ArrayList<>();
            RecipeList recipeList = PantryPal.mainWindow.getRecipeList();
            for (int i = 0; i < PantryPal.recipeStorage.size(); i++){
                Recipe recipe = PantryPal.recipeStorage.get(i);
                list.add(recipe);
            }
            Collections.sort(list, new Comparator<Recipe>() {
                public int compare(Recipe t1, Recipe t2) {
                    String s1 = t1.getTitle();
                    String s2 = t2.getTitle();
                    for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                        if (s1.charAt(i) == s2.charAt(i)) {
                            continue;
                        } else if (s1.charAt(i) > s2.charAt(i)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    if (s1.length() > s2.length()) {
                        return 1;
                    } else if (s1.length() < s2.length()) {
                        return -1;
                    }
                    return 0;
                }
             });
        
            for (int i = 0; i < list.size(); i++){
              ((RecipeBox) recipeList.getChildren().get(i)).replaceRecipe(list.get(i).getTitle());
            }
         
        });

        oldtoNewButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 0;");

        oldtoNewButton.setOnAction(e1 -> {

            RecipeList recipeList = PantryPal.mainWindow.getRecipeList();
            for (int i = 0; i < PantryPal.recipeStorage.size(); i++){
                ((RecipeBox) recipeList.getChildren().get(i)).replaceRecipe(PantryPal.recipeStorage.get(i).getTitle());
                System.out.println(PantryPal.recipeStorage.get(i).getTitle());
            }
            
         
        });





        newtoOldButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 0;");
        // Link newtoOldButton to sort function
        newtoOldButton.setOnAction(e1 -> {
            RecipeList recipeList = PantryPal.mainWindow.getRecipeList();
            int inc = 0;
            for (int i = PantryPal.recipeStorage.size()-1; i >= 0; i--){
                
                ((RecipeBox) recipeList.getChildren().get(inc)).replaceRecipe(PantryPal.recipeStorage.get(i).getTitle());
                inc++;
                System.out.println(PantryPal.recipeStorage.get(i).getTitle());
            }
         
        });

        // Set Sort option
        SortBox = new ComboBox<>();
        SortBox.setPrefSize(150, 20);
        SortBox.getItems().addAll(alphabetButton,oldtoNewButton,newtoOldButton);
        SortBox.setPromptText("Sort By");
        // Add Recipe button
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addRecipeButton = new Button("Add Recipe");
        addRecipeButton.setStyle(defaultButtonStyle);

        restoreRecipes = new Button("Restore recipes");
        restoreRecipes.setStyle(defaultButtonStyle);

        this.getChildren().addAll(titleText, SortBox,addRecipeButton);
        // this.getChildren().add(restoreRecipes);
        
    }
    // getMethod for each defined varaible
    public Button getAddButton() {
        return addRecipeButton;
    }

    public Button getAddRecipeButton() {
        return addRecipeButton;
    }

    public Button getRestoreButton() {
        return restoreRecipes;
    }

    public Button getAlphabetButton(){
        return alphabetButton;
    }

    public Button getNewtoOldButton(){
        return newtoOldButton;
    }
    
    public Button getOldtoNewButton(){
        return oldtoNewButton;
    }
    

    public void sortAphabetically(List <Recipe> recipes) {
        ArrayList<Recipe> list = new ArrayList<>();
        MainWindow mainWindow = new MainWindow(UserSession.getInstance());
        RecipeList recipeList = mainWindow.getRecipeList();
        for (int i = 0; i < recipes.size(); i++){
            Recipe recipe = recipes.get(i);
            list.add(recipe);
        }
        Collections.sort(list, new Comparator<Recipe>() {
            public int compare(Recipe t1, Recipe t2) {
                String s1 = t1.getTitle();
                String s2 = t2.getTitle();
                for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                    if (s1.charAt(i) == s2.charAt(i)) {
                        continue;
                    } else if (s1.charAt(i) > s2.charAt(i)) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                if (s1.length() > s2.length()) {
                    return 1;
                } else if (s1.length() < s2.length()) {
                    return -1;
                }
                return 0;
            }
        });
        
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).getTitle());
            ((RecipeBox) recipeList.getChildren().get(i)).replaceRecipe(list.get(i).getTitle());
        }
    }

 
        
    
    
}

/**
 * Main window layout
 */
public class MainWindow extends BorderPane {
    private MainWindowHeader mainWindowHeader; // header section
    private RecipeList recipeList;
    private ScrollPane scrollPane;

    public MainWindow(UserSession userSession) {
        mainWindowHeader = new MainWindowHeader();
        recipeList = new RecipeList(userSession);

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

    public void setRecipeList (RecipeList recipeList){
        this.recipeList = recipeList;
    }

     
}

/**
 * A Recipe in RecipeList (App display)
 */
class RecipeBox extends HBox {
    private Button title;
    private String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    private Button deleteButton;

    

    RecipeBox(String title, UserSession userSession) {
        // this.setPrefSize(1140, 50);

        // Set recipe appearance
        this.title = new Button();
        this.title.setPrefSize(800, 50);
        this.title.setText(title);
       
        this.title.setStyle(defaultButtonStyle);
        this.getChildren().add(this.title);

        // Set up delete button apperance
        this.deleteButton = new Button("delete");
        this.deleteButton.setPrefSize(350, 50);

        // deletes the recipe from the list
        this.deleteButton.setOnAction(e -> {
            Recipe Rp = getRecipeByTitle(this.title.getText());
            PantryPal.recipeStorage.remove(Rp);
            try {
                RecipeList List = (RecipeList) getParent();
                List.getChildren().remove(this);
            } catch (NullPointerException error) {
            }

            MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
            mongoClient.deleteRecipe(title);
        });
        // adds the delete button
        this.getChildren().add(this.deleteButton);

        // Creates a new RecipeDetailsView window using selected recipe
        this.title.setOnAction(e -> {
            Recipe recipe = getRecipeByTitle(this.title.getText());
            if (recipe != null) {
                ViewWindow recipeDetailsView = new ViewWindow(recipe, this.title.getScene(), userSession);
                Scene recipeDetailsScene = new Scene(recipeDetailsView, 500, 400);
                Stage stage = (Stage) this.getScene().getWindow();
                double height = stage.getHeight();
                double width = stage.getWidth();
                boolean fullscreen = stage.isFullScreen();
                stage.setScene(recipeDetailsScene);
                if (fullscreen == true) {
                    stage.setFullScreen(fullscreen);
                } else {
                    stage.setHeight(height);
                    stage.setWidth(width);
                }

            } else {
                System.out.println("no recipe");
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
     public void replaceRecipe(String title){
            this.title.setText(title);
    }
   
}

/**
 * The main recipe list displayed in app.
 */
class RecipeList extends VBox {
    private UserSession userSession;

    RecipeList(UserSession userSession) {
        this.userSession = userSession;
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(1440, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    RecipeList (){
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(1440, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
    // Adds a single recipe to the main list given the title of recipe
    public void addRecipe(String title) {
        this.getChildren().add(0, new RecipeBox(title, userSession)); // add new recipe to top of list
    }

    
    public void deleteAllRecipe () {

        for (int i = 0; i < this.getChildren().size(); i++){
            this.getChildren().remove(i);
        }
        
    }
}



