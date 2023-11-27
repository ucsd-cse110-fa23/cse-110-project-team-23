package project.Client.CreateAccountWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Database.UserAuthentication;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class CreateAccountWindowBody extends VBox {
    private Label usernameLabel;
    private TextField username;
    private Label passwordLabel;
    private PasswordField password;
    private Button createAccountButton;
    private CheckBox rememberMeCheckBox;
    private Stage primaryStage;
    private Scene targetScene;

    public CreateAccountWindowBody(Stage primaryStage, Scene targetScene) {
        this.primaryStage = primaryStage;
        this.targetScene = targetScene;
        this.setStyle("-fx-background-color: #F0F8FF;");
        usernameLabel = new Label();
        usernameLabel.setText("Username");
        usernameLabel.setPrefSize(100, 30);
        username = new TextField();
        username.setPrefSize(400, 50);

        passwordLabel = new Label();
        passwordLabel.setText("Password");
        passwordLabel.setPrefSize(100, 30);

        password = new PasswordField();
        password.setPrefSize(400, 50);

        createAccountButton = new Button("Create Account");
        rememberMeCheckBox = new CheckBox("Remember Me");

        createAccountButton.setOnAction(e -> {
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
                if (userAuth.createAccount()) {
                    primaryStage.setScene(targetScene);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Username already exists. Please choose a different username");
                    alert.showAndWait();
                    return;
                }
            }

        });

        this.getChildren().addAll(usernameLabel, username, passwordLabel, password, createAccountButton,
                rememberMeCheckBox);
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public void clear() {
        username.clear();
        password.clear();
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public CheckBox getRememberMeCheckBox() {
        return rememberMeCheckBox;
    }
}
