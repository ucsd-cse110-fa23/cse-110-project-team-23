package project.Client.LoginWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Client.MainWindow.RecipeList;
import project.Database.MongoDBClient;
import project.Database.UserAuthentication;
import project.Database.UserSession;
import project.Server.Recipe;

import java.util.*;

public class LoginWindowBody extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginWindowBody(Stage primaryStage, Scene targetScene, UserSession userSession, RecipeList recipeList,
            List<Recipe> recipeStorage) {
        this.setStyle("-fx-background-color: #F0F8FF;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String enteredUsername = getUsername();
            String enteredPassword = getPassword();

            // Validate that username and password are not empty
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter both username and password.");
                alert.showAndWait();
                return;
            } else {
                UserAuthentication userAuth = new UserAuthentication(enteredUsername, enteredPassword);
                int result = userAuth.login();
                if (result == 1) {
                    primaryStage.setScene(targetScene);
                    userSession.setUsername(enteredUsername);
                    MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
                    try {
                        mongoClient.openRecipes(recipeList, recipeStorage);
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                } else if (result == 2) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password or username is incorrect");
                    alert.showAndWait();
                    return;
                } else if (result == 3) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("User not Found");
                    alert.showAndWait();
                    return;
                }
            }

        });

        this.getChildren().addAll(usernameField, passwordField, loginButton);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void clear() {
        usernameField.clear();
        passwordField.clear();
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
