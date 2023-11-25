package project;

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
import java.util.*;

public class LoginWindow extends BorderPane {
    private LoginWindowHeader header;
    private LoginWindowBody body;

    public LoginWindow(Stage primaryStage, Scene targScene, UserSession userSession, RecipeList recipeList,
            List<Recipe> recipeStorage) {
        header = new LoginWindowHeader();
        body = new LoginWindowBody(primaryStage, targScene, userSession, recipeList, recipeStorage);

        this.setTop(header);
        this.setCenter(body);
    }

    public LoginWindowHeader getLoginWindowHeader() {
        return header;
    }

    public LoginWindowBody getLoginWindowBody() {
        return body;
    }
}

class LoginWindowHeader extends HBox {
    private Text titleText;
    private Button returnButton;

    LoginWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        titleText = new Text("Login"); // Text of the Header
        titleText.setFont(new Font(20));
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        returnButton = new Button("Return");

        this.getChildren().addAll(titleText, returnButton);
    }

    public Text getTitleText() {
        return titleText;
    }

    public Button getReturnButton() {
        return returnButton;
    }
}

class LoginWindowBody extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    LoginWindowBody(Stage primaryStage, Scene targetScene, UserSession userSession, RecipeList recipeList,
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
                    mongoClient.openRecipes(recipeList, recipeStorage);
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
