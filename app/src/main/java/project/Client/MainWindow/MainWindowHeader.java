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
import java.util.function.Predicate;
import ch.qos.logback.core.filter.Filter;
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
    private ComboBox<String> FilterComboBox;



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
        sortComboBox.getSelectionModel().select("Sort Recipes");

        FilterComboBox  = new ComboBox<>();
        FilterComboBox.setPrefSize(150, 20);
        FilterComboBox.getItems().addAll("Default", "Breakfast", "Lunch", "Dinner");
        FilterComboBox.getSelectionModel().select("Filter Meal Type");

        FilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                filterByMealType(newValue);
        });

        // Apply sort/filter button
        applyButton = new Button("Apply");
        applyButton.setOnAction(e -> {
            performAction(sortComboBox.getValue());

        });



        // Add Recipe button
        addRecipeButton = new Button("Add Recipe");

        this.getChildren().addAll( sortComboBox, applyButton,  titleText, FilterComboBox, addRecipeButton);

        
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
        ArrayList<Recipe> list = filterByMealType(FilterComboBox.getValue());
        ArrayList<Recipe> sortedList;
        if (sortAction.equals("Latest (default)")|| sortAction.equals("Sort Recipes")) {
            sortedList = sortNew(list);
        } else if (sortAction.equals("Oldest")) {
            sortedList = sortOld(list);
            
        } else if (sortAction.equals("A-Z")) {
            sortedList = sortAlpha(list);
        }
        else{
            sortedList = list;
        }
        recipeList.getChildren().clear();
        updateRecipeList(sortedList, recipeList);
        ;
    }

    public ArrayList<Recipe> filterByMealType(String selectedMealType){
       RecipeList recipeList = PantryPal.mainWindow.getRecipeList();
       recipeList.getChildren().clear();
       ArrayList<Recipe> list;
        if(selectedMealType.equals("Default")|| selectedMealType.equals("Filter Meal Type")){
             list = PantryPal.recipeStorage;
            updateRecipeList(list, recipeList);
            return list;
        }
        if (selectedMealType.equals("Breakfast")) {
            list = FilterbyBreakfast(PantryPal.recipeStorage);
            updateRecipeList(list, recipeList);
            return list;
        } else if (selectedMealType.equals("Lunch")) {
            list = FilterbyLunch(PantryPal.recipeStorage);
            updateRecipeList(list, recipeList);
            return list;
        } else if (selectedMealType.equals("Dinner")) {
            list  = FilterbyDinner(PantryPal.recipeStorage);
           updateRecipeList(list, recipeList);
           return list;
        }
        else{
            return null;
        }
        
    }

    private void updateRecipeList(ArrayList<Recipe> list, RecipeList recipelist){
        for(Recipe recipe : list){
            RecipeBox recipeBox = new RecipeBox(recipe.getTitle(), null);
            recipelist.getChildren().add(recipeBox);
        }
    }

    public static ArrayList<Recipe> FilterbyBreakfast(ArrayList<Recipe> recipeStorage){
        ArrayList<Recipe> list = new ArrayList<>();
        for (int i = 0; i < recipeStorage.size(); i++){
            Recipe recipe = recipeStorage.get(i);
            if(recipe.getMealType().toLowerCase().contains("breakfast")){
            list.add(recipe);
            }
        }
        return list; 
    }

        public static ArrayList<Recipe> FilterbyLunch(ArrayList<Recipe> recipeStorage){
        ArrayList<Recipe> list = new ArrayList<>();
        for (int i = 0; i < recipeStorage.size(); i++){
            Recipe recipe = recipeStorage.get(i);
            if(recipe.getMealType().toLowerCase().contains("lunch")){
            list.add(recipe);
            }
        }
        return list; 
    }

    public static ArrayList<Recipe> FilterbyDinner(ArrayList<Recipe> recipeStorage){
        ArrayList<Recipe> list = new ArrayList<>();
        for (int i = 0; i < recipeStorage.size(); i++){
            Recipe recipe = recipeStorage.get(i);
            if(recipe.getMealType().toLowerCase().contains("dinner")){
            list.add(recipe);
            }
        }
        return list; 
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
        ArrayList<Recipe> list = new ArrayList<>(recipeStorage.size());
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
