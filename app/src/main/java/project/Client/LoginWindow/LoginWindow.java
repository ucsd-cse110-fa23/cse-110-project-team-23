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