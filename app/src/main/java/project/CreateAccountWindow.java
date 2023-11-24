package project;

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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class CreateAccountWindow extends BorderPane {
    private CreateAccountHeader header;
    private CreateAccountBody body;
    private Stage primaryStage;
    private Scene targetScene;

    public CreateAccountWindow(Stage primaryStage, Scene targetScene) {
        this.primaryStage = primaryStage;
        this.targetScene = targetScene;
        header = new CreateAccountHeader();
        body = new CreateAccountBody(primaryStage, targetScene);

        this.setTop(header);
        this.setCenter(body);
    }

    public CreateAccountHeader getCreateAccountHeader() {
        return header;
    }

    public CreateAccountBody getCreateAccountBody() {
        return body;
    }
}

class CreateAccountHeader extends HBox {
    private Button returnButton;

    CreateAccountHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Create Account"); // Text of the Header
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

class CreateAccountBody extends VBox {
    private Label usernameLabel;
    private TextField username;
    private Label passwordLabel;
    private PasswordField password;
    private Button createAccountButton;
    private CheckBox rememberMeCheckBox;
    private Stage primaryStage;
    private Scene targetScene;

    CreateAccountBody(Stage primaryStage, Scene targetScene) {
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
