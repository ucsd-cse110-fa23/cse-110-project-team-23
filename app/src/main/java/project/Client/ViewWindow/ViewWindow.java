package project.Client.ViewWindow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import project.Database.MongoDBClient;
import project.Database.UserSession;
import project.Server.Recipe;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;

import java.util.*;

/**
 * Recipe details window layout
 */
public class ViewWindow extends BorderPane {
    private TextArea descriptionTextArea;
    private RecipeViewFooter footer;
    private RecipeViewHeader header;

    public ViewWindow(Recipe recipe, Scene previousScene, UserSession userSession) {
        header = new RecipeViewHeader(recipe);
        footer = new RecipeViewFooter();
        this.setStyle("-fx-background-color: #F0F8FF;");

        String recipeDescription = recipe.getDescription();
        descriptionTextArea = new TextArea(recipeDescription);
        descriptionTextArea.setPrefSize(400, 300);

        // Return button retrieves current stage and returns to the previous scene
        // (main)
        footer.getReturnButton().setOnAction(e -> {
            Stage stage = (Stage) footer.getReturnButton().getScene().getWindow();
            double height = stage.getHeight();
            double width = stage.getWidth();
            boolean fullscreen = stage.isFullScreen();
            stage.setScene(previousScene);
            if (fullscreen == true) {
                stage.setFullScreen(fullscreen);
            } else {
                stage.setHeight(height);
                stage.setWidth(width);
            }
        });

        this.setTop(header);
        this.setCenter(descriptionTextArea);
        this.setBottom(footer);
        descriptionTextArea.setPrefSize(400, 300);

        footer.getSaveButton().setOnAction(e -> {
            recipe.setDescription(this.getDescription());
            MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
            mongoClient.editRecipe(recipe.getTitle(), this.getDescription());
            Stage stage = (Stage) footer.getSaveButton().getScene().getWindow();
            double height = stage.getHeight();
            double width = stage.getWidth();
            boolean fullscreen = stage.isFullScreen();
            stage.setScene(previousScene);
            if (fullscreen == true) {
                stage.setFullScreen(fullscreen);
            } else {
                stage.setHeight(height);
                stage.setWidth(width);
            }
        });
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }
}
