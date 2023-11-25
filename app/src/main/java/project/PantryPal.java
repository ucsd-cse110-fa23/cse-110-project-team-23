package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;

import java.util.*;

/**
 * References:
 * https://stackoverflow.com/questions/55389052/how-to-set-javafx-textfield-to-wrap-text-inside-field
 * 
 */

public class PantryPal extends Application {
    public static List<Recipe> recipeStorage;
    private UserSession userSession;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize new user session
        userSession = UserSession.getInstance();

        // Initiate recipe storage
        recipeStorage = new ArrayList<>();
        // File storageCSV = new File("recipes.csv");

        // Setting the layout of the MainWindow
        MainWindow mainWindow = new MainWindow(userSession);
        Scene mainScene = new Scene(mainWindow, 800, 400);

        // Setting OpenAppWindow
        OpenAppWindow openAppWindow = new OpenAppWindow();
        Scene openAppScene = new Scene(openAppWindow, 800, 400);

        // Setting login Window
        LoginWindow loginWindow = new LoginWindow(primaryStage, mainScene, userSession, mainWindow.getRecipeList(),
                recipeStorage);
        Scene loginScene = new Scene(loginWindow, 800, 400);

        // Setting createAccount window
        CreateAccountWindow createAccountWindow = new CreateAccountWindow(primaryStage, loginScene);
        Scene createAccountScene = new Scene(createAccountWindow, 800, 400);

        // Setting the layout of the AddWindow
        AddWindow addWindow = new AddWindow();
        Scene addScene = new Scene(addWindow);

        // Setting the layout of the SuggestWindow
        SuggestWindow suggestWindow = new SuggestWindow();
        Scene suggestScene = new Scene(suggestWindow);

        SuggestWindowBody suggestWindowBody = suggestWindow.getSuggestWindowBody();

        Button confirmButton = suggestWindowBody.getConfirmButton();
        confirmButton.setOnAction(e -> {
            if (suggestWindow.getSuggestWindowBody().getIngredients().trim().isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No Ingredients");
                alert.setHeaderText("No Ingredients Entered");
                alert.setContentText("Please enter or record ingredients before confirming");

                alert.showAndWait();
            } else {
                try {
                    String mealType = suggestWindow.getSuggestWindowBody().getMealType();
                    String ingredients = suggestWindow.getSuggestWindowBody().getIngredients();
                    String instructionString = "meal type: " + mealType + " Ingredients: " + ingredients;
                    ChatAPI instruction = new ChatAPI(instructionString);
                    String suggestedrecipe = instruction.suggestRecipe();
                    double height = primaryStage.getHeight();
                    double width = primaryStage.getWidth();
                    boolean fullscreen = primaryStage.isFullScreen();
                    primaryStage.setScene(addScene);
                    if (fullscreen == true) {
                        primaryStage.setFullScreen(fullscreen);
                    } else {
                        primaryStage.setHeight(height);
                        primaryStage.setWidth(width);
                    }

                    suggestWindow.getSuggestWindowBody().clear();
                    addWindow.getAddWindowBody().setRecipe(suggestedrecipe);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        });
        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(openAppScene);

        // Link goCreateAccButton button with its function
        Button goCreateAccButon = openAppWindow.getCreateAccountButton();
        goCreateAccButon.setOnAction(e -> {
            primaryStage.setScene(createAccountScene);
        });

        // Link goLoginButton button with its function
        Button goLoginButton = openAppWindow.getLoginButton();
        goLoginButton.setOnAction(e -> {
            primaryStage.setScene(loginScene);
        });

        // Link return buttons in CreateAccount and Login with functionality
        Button createAccountBackButton = createAccountWindow.getCreateAccountHeader().getReturnButton();
        createAccountBackButton.setOnAction(e -> {
            primaryStage.setScene(openAppScene);
        });

        Button loginReturnButton = loginWindow.getLoginWindowHeader().getReturnButton();
        loginReturnButton.setOnAction(e -> {
            primaryStage.setScene(openAppScene);
        });

        // Link addRecipeButton with its function
        Button addRecipeButton = mainWindow.getHeader().getAddButton();
        addRecipeButton.setOnAction(e -> {
            double height = primaryStage.getHeight();
            double width = primaryStage.getWidth();
            boolean fullscreen = primaryStage.isFullScreen();
            primaryStage.setScene(suggestScene);
            if (fullscreen == true) {
                primaryStage.setFullScreen(fullscreen);
            } else {
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
            }
        });

        // Link returnButton with its function
        Button returnButton = addWindow.getAddWindowHeader().getReturnButton();
        AddWindowBody addWindowBody = addWindow.getAddWindowBody();
        returnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            addWindowBody.clear();
            double height = primaryStage.getHeight();
            double width = primaryStage.getWidth();
            boolean fullscreen = primaryStage.isFullScreen();
            primaryStage.setScene(mainScene);
            if (fullscreen == true) {
                primaryStage.setFullScreen(fullscreen);
            } else {
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
            }
        });

        // Link returnButton in SuggestWindow with its function
        Button returnSuggestButton = suggestWindow.getSuggestWindowHeader().getReturnButton();
        returnSuggestButton.setOnAction(e -> {
            double height = primaryStage.getHeight();
            double width = primaryStage.getWidth();
            boolean fullscreen = primaryStage.isFullScreen();
            primaryStage.setScene(mainScene);
            if (fullscreen == true) {
                primaryStage.setFullScreen(fullscreen);
            } else {
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
            }
        });

        // Link completeButton with its function
        Button completeButton = addWindow.getAddWindowFooter().getCompleteButton();
        completeButton.setOnAction(e -> {
            String title = addWindowBody.getTitle();
            String description = addWindowBody.getDescription();
            String mealType = addWindowBody.getMealType();
            Recipe newRecipe = new Recipe(title, description, mealType);
            MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
            mongoClient.insertRecipe(title, mealType, description);
            RecipeList recipeList = mainWindow.getRecipeList();

            // Store recipe in storage for view/delete/edit
            recipeStorage.add(newRecipe);
            recipeList.addRecipe(title);

            // Clear text in addWindow
            addWindowBody.clear();

            // Switch back to mainScene
            double height = primaryStage.getHeight();
            double width = primaryStage.getWidth();
            boolean fullscreen = primaryStage.isFullScreen();
            primaryStage.setScene(mainScene);
            if (fullscreen == true) {
                primaryStage.setFullScreen(fullscreen);
            } else {
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
            }

        });

        // Make window non-resizable
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(e -> {
            try {
                userSession.clearSession();
            } catch (Exception ex) {

            }
        });
        // Show the app
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}