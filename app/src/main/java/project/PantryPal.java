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
 * References:
 * https://stackoverflow.com/questions/55389052/how-to-set-javafx-textfield-to-wrap-text-inside-field
 * 
 */

public class PantryPal extends Application {
    public static List<Recipe> recipeStorage;
    public boolean start = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initiate recipe storage
        recipeStorage = new ArrayList<>();
        File storageCSV = new File("recipes.csv");
        // Setting the layout of the MainWindow
        MainWindow mainWindow = new MainWindow();
        Scene mainScene = new Scene(mainWindow, 500, 400);

        // Setting the layout of the AddWindow
        AddWindow addWindow = new AddWindow();
        Scene addScene = new Scene(addWindow, 500, 400);

        // Setting the layout of the SuggestWindow
        SuggestWindow suggestWindow = new SuggestWindow();
        Scene suggestScene = new Scene(suggestWindow, 500, 400);

        SuggestWindowBody suggestWindowBody = suggestWindow.getSuggestWindowBody();

        Button confirmButton = suggestWindowBody.getConfirmButton();
        confirmButton.setOnAction(e -> {
            try {
                ChatAPI instruction = new ChatAPI(suggestWindow.getSuggestWindowBody().getIngredients());
                String suggestedrecipe = instruction.suggestRecipe();
                double height = primaryStage.getHeight();
                double width = primaryStage.getWidth();
                boolean fullscreen = primaryStage.isFullScreen();
                primaryStage.setScene(addScene);
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
                
                
                suggestWindow.getSuggestWindowBody().clear();
                addWindow.getAddWindowBody().setRecipe(suggestedrecipe);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(mainScene);

        // Link addRecipeButton with its function
        Button addRecipeButton = mainWindow.getHeader().getAddButton();
        addRecipeButton.setOnAction(e -> {
            double height = primaryStage.getHeight();
            boolean fullscreen = primaryStage.isFullScreen();
            double width  = primaryStage.getWidth();
            primaryStage.setScene(suggestScene);
            
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
        });

        // Link returnButton with its function
        Button returnButton = addWindow.getAddWindowHeader().getReturnButton();
        AddWindowBody addWindowBody = addWindow.getAddWindowBody();
        returnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            addWindowBody.clear();
            primaryStage.setScene(mainScene);
        });

        // Link returnButton in SuggestWindow with its function
        Button returnSuggestButton = suggestWindow.getSuggestWindowHeader().getReturnButton();
        returnSuggestButton.setOnAction(e -> primaryStage.setScene(mainScene));

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


        //Button restoreButton = mainWindow.getHeader().getRestoreButton();
        primaryStage.setOnShown(e -> {
            if (start) {
                try {
                    FileReader csvReader = new FileReader(storageCSV);
                    int curr = 0;
                    String file = "";
                    while(curr != -1) {
                        file += (char) curr;
                        curr = csvReader.read();
                    }
                    csvReader.close();
                    file = file.substring(1);
                    while(true) {
                        String recipe = "";
                        int recipeIndex = file.indexOf("$");
                        if (recipeIndex != -1) {
                            recipe = file.substring(0, recipeIndex);
                        } else {
                            break;
                        }
                        int firstAt = recipe.indexOf('@');
                        int secondAt = recipe.indexOf('@', firstAt + 1);
                        String title = recipe.substring(0, firstAt).trim();
                        String mealType = recipe.substring(firstAt + 1, secondAt).trim();
                        String description = recipe.substring(secondAt + 1).trim();
                        Recipe newRecipe = new Recipe(title, description, mealType);
                        RecipeList recipeList = mainWindow.getRecipeList();
                        recipeStorage.add(newRecipe);
                        recipeList.addRecipe(title);
                        file = file.substring(recipeIndex + 1);
                    }
                } catch (Exception ex) {

                }
                start = false;
                primaryStage.setScene(mainScene);
            }
        });


        // Make window non-resizable
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(e -> {
            try {
                storageCSV.delete();
                storageCSV.createNewFile();
                FileWriter csvWriter = new FileWriter(storageCSV);
                for (Recipe r : recipeStorage) {
                    String line = r.getTitle() + "@" + r.getMealType() + "@" + r.getDescription() + "$\n"; 
                    csvWriter.write(line);
                }
                csvWriter.close();
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