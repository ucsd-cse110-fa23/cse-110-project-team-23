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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
    private UserSession userSession;
    
    public ViewWindow(UserSession userSession) {
        header = new RecipeViewHeader();
        footer = new RecipeViewFooter();
        this.userSession = userSession;
        this.setStyle("-fx-background-color: #F0F8FF;");

        //String recipeDescription = recipe.getDescription();
        descriptionTextArea = new TextArea();

        this.setTop(header);
        this.setCenter(descriptionTextArea);
        this.setBottom(footer);
        descriptionTextArea.setPrefSize(400, 300);
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }

    public RecipeViewHeader getRecipeViewHeader(){
        return this.header;
    }

    public RecipeViewFooter getRecipeViewFooter(){
        return this.footer;
    }

    public void setStuff(Recipe recipe, Scene previousScene){
        this.header.setTitle(recipe.getTitle());
        this.header.setImageURL(recipe.getImageURL());
        this.descriptionTextArea.setText(recipe.getDescription());
        footer.getSaveButton().setOnAction(e -> {
            recipe.setDescription(this.getDescription());
            //MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
            //mongoClient.editRecipe(recipe.getTitle(), this.getDescription());
            Stage stage = (Stage) footer.getSaveButton().getScene().getWindow();
            double height = stage.getHeight();
            double width = stage.getWidth();
            boolean fullscreen = stage.isFullScreen();
            try {
                stage.setScene(previousScene);
            } catch (Exception ex) {

            }
            if (fullscreen == true) {
                stage.setFullScreen(fullscreen);
            } else {
                stage.setHeight(height);
                stage.setWidth(width);
            }
        });

        footer.getReturnButton().setOnAction(e -> {
            Stage stage = (Stage) footer.getReturnButton().getScene().getWindow();
            double height = stage.getHeight();
            double width = stage.getWidth();
            boolean fullscreen = stage.isFullScreen();
            try {
                stage.setScene(previousScene);
            } catch (Exception ex) {

            }
            
            if (fullscreen == true) {
                stage.setFullScreen(fullscreen);
            } else {
                stage.setHeight(height);
                stage.setWidth(width);
            }
        });
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent c = new ClipboardContent();
        c.putString(content);
        clipboard.setContent(c);
    }
    public UserSession getUserSession(){
        return this.userSession;
    }
}
