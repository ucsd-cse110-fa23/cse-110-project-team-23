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

public class RecipeViewHeader extends HBox {
    private Text titleText;
    private ImageView recipeImageView;

    public RecipeViewHeader(Recipe recipe) {
        // Create and set the recipe title
        titleText = new Text(recipe.getTitle());
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        // Create and set the recipe image space
        recipeImageView = new ImageView();
        recipeImageView.setFitWidth(200); // Set the width of the image
        recipeImageView.setFitHeight(100); // Set the height of the image
        recipeImageView.setPreserveRatio(true); // Preserve the image's aspect ratio

        // Fetch the image URL from the recipe
        String imageURL = recipe.getImageURL();

        // Check if imageURL is not null or empty before attempting to load the image
        if (imageURL != null && !imageURL.isEmpty()) {
            try {
                Image image = new Image(imageURL);
                recipeImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Add the title and image to the header
        this.getChildren().addAll(titleText, recipeImageView);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }
}
