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

public class LoginWindowHeader extends HBox {
    private Text titleText;
    private Button returnButton;

    public LoginWindowHeader() {
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
