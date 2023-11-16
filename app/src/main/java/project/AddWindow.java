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
 * Header segment for AddWindow
 */
class AddWindowHeader extends HBox {
    private Button returnButton;

    AddWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Add Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(defaultButtonStyle);
        this.getChildren().addAll(titleText, returnButton);
    }

    public Button getReturnButton() {
        return returnButton;
    }
}

/**
 * Center segment for AddWindow
 */
class AddWindowBody extends VBox {
    private Label titleLabel;
    private TextField title;
    private Label mealLabel;
    private TextField mealType;
    private Label descriptionLabel;
    private TextArea description;

    AddWindowBody() {
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
    public void clear() {
        title.clear();
        description.clear();
    }

    public void setRecipe(String suggestedrecipe){
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
}

/**
 * Footer segment for AddWindow
 */
class AddWindowFooter extends HBox {
    private Button completeButton;

    AddWindowFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        completeButton = new Button();
        completeButton.setText("Complete");

        this.getChildren().addAll(completeButton);
    }

    public Button getCompleteButton() {
        return completeButton;
    }
}

/**
 * Add window layout
 */
public class AddWindow extends BorderPane {
    private AddWindowHeader header;
    private AddWindowBody body;
    private AddWindowFooter footer;

    public AddWindow() {
        header = new AddWindowHeader();
        body = new AddWindowBody();
        footer = new AddWindowFooter();

        this.setTop(header);
        this.setCenter(body);
        this.setBottom(footer);
    }

    public AddWindowHeader getAddWindowHeader() {
        return header;
    }

    public AddWindowBody getAddWindowBody() {
        return body;
    }

    public AddWindowFooter getAddWindowFooter() {
        return footer;
    }
}
