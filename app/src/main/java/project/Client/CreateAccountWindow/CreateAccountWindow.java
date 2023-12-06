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

public class CreateAccountWindow extends BorderPane {
    private CreateAccountWindowHeader header;
    private CreateAccountWindowBody body;
    private Stage primaryStage;
    private Scene targetScene;

    public CreateAccountWindow(Stage primaryStage, Scene targetScene) {
        this.primaryStage = primaryStage;
        this.targetScene = targetScene;
        header = new CreateAccountWindowHeader();
        body = new CreateAccountWindowBody(primaryStage, targetScene);

        this.setTop(header);
        this.setCenter(body);
    }

    public CreateAccountWindowHeader getCreateAccountHeader() {
        return header;
    }

    public CreateAccountWindowBody getCreateAccountBody() {
        return body;
    }
    
}