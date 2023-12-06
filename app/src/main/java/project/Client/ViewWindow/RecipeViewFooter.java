package project.Client.ViewWindow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
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

import java.util.*;

/**
 * Footer segment for RecipeViewWindow
 */
public class RecipeViewFooter extends HBox {
    private Button saveButton;
    private Button returnButton;
    private Button shareButton;

    public RecipeViewFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        saveButton = new Button();
        saveButton.setText("Save");

        returnButton = new Button();
        returnButton.setText("Return");
        
        this.shareButton = new Button();
        shareButton.setText("Share");

        this.getChildren().addAll(saveButton, shareButton, returnButton);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public Button getShareButton(){
        return shareButton;
    }

    public void shareButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.shareButton.setOnAction(eventHandler);
    }
}
