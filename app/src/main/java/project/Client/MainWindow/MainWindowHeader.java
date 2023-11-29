package project.Client.MainWindow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import project.PantryPal;
import project.Client.ViewWindow.ViewWindow;
import project.Database.MongoDBClient;
import project.Database.UserSession;
import project.Server.Recipe;
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
public class MainWindowHeader extends HBox {

    private Button addRecipeButton;
    private Button restoreRecipes;
    private ComboBox<String> sortComboBox;
    private Button applyButton;

    public MainWindowHeader() {
        // Set Header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center


        sortComboBox = new ComboBox<>();
        sortComboBox.setPrefSize(150, 20);
        sortComboBox.getItems().addAll("Latest (default)", "Oldest", "A-Z");
        sortComboBox.getSelectionModel().select("Latest (default)");

        // Apply sort/filter button
        applyButton = new Button("Apply");
        applyButton.setOnAction(e -> {
            performAction(sortComboBox.getValue());
        });


        // Add Recipe button
        addRecipeButton = new Button("Add Recipe");

        this.getChildren().addAll(titleText, sortComboBox, applyButton, addRecipeButton);
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

    public void performAction(String sortAction) {
        RecipeList recipeList = PantryPal.mainWindow.getRecipeList();
        ArrayList<Recipe> list = new ArrayList<>();

        if (sortAction.equals("Latest (default)")) {
            list = sortNew(PantryPal.recipeStorage);
        } else if (sortAction.equals("Oldest")) {
            list = sortOld(PantryPal.recipeStorage);
        } else if (sortAction.equals("A-Z")) {
            list = sortAlpha(PantryPal.recipeStorage);
        }

        for (int i = 0; i < list.size(); i++){
            ((RecipeBox) recipeList.getChildren().get(i)).replaceRecipe(list.get(i).getTitle());
        }
    }


    public static ArrayList<Recipe> sortAlpha(ArrayList<Recipe> recipeStorage) {
        ArrayList<Recipe> list = new ArrayList<>();
        for (int i = 0; i < recipeStorage.size(); i++){
            Recipe recipe = recipeStorage.get(i);
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
        return list;
    }

    public static ArrayList<Recipe> sortOld(ArrayList<Recipe> recipeStorage) {
        return recipeStorage;
    }

    public static ArrayList<Recipe> sortNew(ArrayList<Recipe> recipeStorage) {
        ArrayList<Recipe> list = new ArrayList<>();
        for (int i = recipeStorage.size() - 1; i >= 0; i--) {
            list.add(recipeStorage.get(i));
        }
        return list;
    }

    // public void sortAlphabetically(List <Recipe> recipes) {
    //     ArrayList<Recipe> list = new ArrayList<>();
    //     MainWindow mainWindow = new MainWindow(UserSession.getInstance());
    //     RecipeList recipeList = mainWindow.getRecipeList();
    //     for (int i = 0; i < recipes.size(); i++){
    //         Recipe recipe = recipes.get(i);
    //         list.add(recipe);
    //     }
    //     Collections.sort(list, new Comparator<Recipe>() {
    //         public int compare(Recipe t1, Recipe t2) {
    //             String s1 = t1.getTitle();
    //             String s2 = t2.getTitle();
    //             for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
    //                 if (s1.charAt(i) == s2.charAt(i)) {
    //                     continue;
    //                 } else if (s1.charAt(i) > s2.charAt(i)) {
    //                     return 1;
    //                 } else {
    //                     return -1;
    //                 }
    //             }
    //             if (s1.length() > s2.length()) {
    //                 return 1;
    //             } else if (s1.length() < s2.length()) {
    //                 return -1;
    //             }
    //             return 0;
    //         }
    //     });
        
    //     for (int i = 0; i < list.size(); i++){
    //         System.out.println(list.get(i).getTitle());
    //         ((RecipeBox) recipeList.getChildren().get(i)).replaceRecipe(list.get(i).getTitle());
    //     }
    // }
}
