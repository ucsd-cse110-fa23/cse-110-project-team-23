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

//import com.sun.glass.ui.View;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;



/**
 * Main window layout
 */
public class MainWindow extends BorderPane {
    private MainWindowHeader mainWindowHeader; // header section
    private RecipeList recipeList;
    private ScrollPane scrollPane;
    private ViewWindow recipeDetailView;

    public MainWindow(UserSession userSession, ViewWindow recipeDetailView) {
        mainWindowHeader = new MainWindowHeader();
        recipeList = new RecipeList(userSession, recipeDetailView);

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

    public ViewWindow getViewWindow(){
        return this.recipeDetailView;
    }
}



