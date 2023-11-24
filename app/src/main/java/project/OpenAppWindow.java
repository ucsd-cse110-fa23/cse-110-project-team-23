package project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OpenAppWindow extends VBox {
    private Button createAccountButton;
    private Button loginButton;

    public OpenAppWindow() {
        // Set appearance
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        // Create header
        Text headerText = new Text("Welcome to PantryPal");
        headerText.setFont(new Font(20));

        // Create buttons
        createAccountButton = new Button("Create Account");
        loginButton = new Button("Login");

        // Add header and buttons to the layout
        this.getChildren().addAll(headerText, createAccountButton, loginButton);
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}