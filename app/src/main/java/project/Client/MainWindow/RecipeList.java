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
 * The main recipe list displayed in app.
 */
public class RecipeList extends VBox {
    private UserSession userSession;

    public RecipeList(UserSession userSession) {
        this.userSession = userSession;
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(1440, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public RecipeList (){
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
