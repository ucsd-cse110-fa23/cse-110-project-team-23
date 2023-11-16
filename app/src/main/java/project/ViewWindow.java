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
 * Footer segment for RecipeViewWindow
 */
class RecipeViewFooter extends HBox {
    private Button saveButton;
    private Button returnButton;

    RecipeViewFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        saveButton = new Button();
        saveButton.setText("Save");

        returnButton = new Button();
        returnButton.setText("Return");

        this.getChildren().addAll(saveButton, returnButton);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

}

/**
 * Recipe details window layout
 */
public class ViewWindow extends BorderPane {
    private TextArea descriptionTextArea;
    private RecipeViewFooter footer;

    public ViewWindow(Recipe recipe, Scene previousScene) {
        footer = new RecipeViewFooter();
        this.setStyle("-fx-background-color: #F0F8FF;");

        String recipeTitle = recipe.getTitle();
        Text titleText = new Text(recipeTitle);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        String recipeDescription = recipe.getDescription();
        descriptionTextArea = new TextArea(recipeDescription);
        descriptionTextArea.setPrefSize(400, 300);

        // Return button retrieves current stage and returns to the previous scene
        // (main)
        footer.getReturnButton().setOnAction(e -> {
            Stage stage = (Stage) footer.getReturnButton().getScene().getWindow();
            stage.setScene(previousScene);
        });

        this.setTop(titleText);
        this.setCenter(descriptionTextArea);
        this.setBottom(footer);
        descriptionTextArea.setPrefSize(400, 300);

        footer.getSaveButton().setOnAction(e -> {
            recipe.setDescription(this.getDescription());
            Stage stage = (Stage) footer.getSaveButton().getScene().getWindow();
            stage.setScene(previousScene);
        });
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }
}
