package project.Client.AddWindow;

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
 * Center segment for AddWindow
 */
public class AddWindowBody extends VBox {
    private Label titleLabel;
    private TextField title;
    private Label mealLabel;
    private TextField mealType;
    private Label descriptionLabel;
    private TextArea description;
    private String imageURL;

    public AddWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");
        titleLabel = new Label();
        titleLabel.setText("Title");
        titleLabel.setPrefSize(100, 30);
        title = new TextField();
        title.setPrefSize(400, 50);

        mealLabel = new Label();
        mealLabel.setText("Meal Type");
        mealLabel.setPrefSize(100, 30);

        mealType = new TextField();
        mealType.setPrefSize(400, 50);

        descriptionLabel = new Label();
        descriptionLabel.setText("Description");

        description = new TextArea();
        description.setPrefSize(200, 300);

        this.getChildren().addAll(titleLabel, title, mealLabel, mealType, descriptionLabel, description);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getMealType() {
        return mealType.getText();
    }

    public String getDescription() {
        return description.getText();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void clear() {
        title.clear();
        description.clear();
    }

    public void setRecipe(String suggestedrecipe) {
        int recipeTitleIdx = suggestedrecipe.indexOf("Recipe Title:");
        int mealTypeIdx = suggestedrecipe.indexOf("Meal Type:");
        int recipeInstructionsIdx = suggestedrecipe.indexOf("Recipe Instructions:");
        String parseTitle = suggestedrecipe.substring(recipeTitleIdx + 14, mealTypeIdx).trim();
        String parseMealType = suggestedrecipe.substring(mealTypeIdx + 11, recipeInstructionsIdx).trim();
        String parseInstruction = suggestedrecipe.substring(recipeInstructionsIdx + 21).trim();
        title.setText(parseTitle);
        mealType.setText(parseMealType);
        description.setText(parseInstruction);
    }

    public void setImageURL(String url) {
        imageURL = url;
    }
}