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

import java.util.*;

/**
 * Header segment of main window, displays Recipe List and the AddRecipe button
 */
class MainWindowHeader extends HBox {

    private Button addRecipeButton;
    private Button restoreRecipes;

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

        restoreRecipes = new Button("Restore recipes");
        restoreRecipes.setStyle(defaultButtonStyle);

        this.getChildren().addAll(titleText, addRecipeButton);
        // this.getChildren().add(restoreRecipes);
    }

    public Button getAddButton() {
        return addRecipeButton;
    }

    public Button getAddRecipeButton() {
        return addRecipeButton;
    }

    public Button getRestoreButton() {
        return restoreRecipes;
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
            Recipe recipe = getRecipeByTitle(title);
            if (recipe != null) {
                ViewWindow recipeDetailsView = new ViewWindow(recipe, this.title.getScene());
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
    private UserSession userSession;

    RecipeList(UserSession userSession) {
        this.userSession = userSession;
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(1440, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    // Adds a single recipe to the main list given the title of recipe
    public void addRecipe(String title) {
        this.getChildren().add(0, new RecipeBox(title, userSession)); // add new recipe to top of list
    }
}
