package project.Server.Client;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import project.Client.CreateAccountWindow.*;
import project.Client.LoginWindow.*;
import project.Client.MainWindow.*;
import project.Database.*;
import project.Server.Recipe;

import java.util.*;

public class ClientController {
    private CreateAccountWindow view1;
    private LoginWindow view2;
    private Stage primaryStage;
    private ClientModel model;
    private UserSession userSession;
    private List<Recipe> recipeStorage;

    public ClientController(CreateAccountWindow view1, LoginWindow view2, Stage primaryStage, ClientModel model) {
        this.view1 = view1;
        this.view2 = view2;
        this.primaryStage = primaryStage;
        this.model = model;

        this.view1.getCreateAccountBody().createAccountButtonAction(this::handleCreateAccountButton);
        this.view2.getLoginWindowBody().loginButtonAction(this::handleLoginButton);
        this.primaryStage.setOnCloseRequest(this::handleClose);
    }

    private void handleCreateAccountButton(ActionEvent event) {
        String username = view1.getCreateAccountBody().getUsername();
        String password = view1.getCreateAccountBody().getPassword();

        String response = model.performRequest("POST", username, password, "NULL_RECIPE", "create");
        boolean canCreate = Boolean.parseBoolean(response);
        if (canCreate){
            Stage stage = view1.getCreateAccountBody().getPrimaryStage();
            Scene scene = view1.getCreateAccountBody().getTargetScene();
            stage.setScene(scene);
        } else {
            view1.getCreateAccountBody().showAlert("Alert", "Username unavailiable");
        }

        view1.getCreateAccountBody().showAlert("Response from get: ", response);
        
    }

    private void handleLoginButton(ActionEvent event) {
        String username = view2.getLoginWindowBody().getUsername();
        String password = view2.getLoginWindowBody().getPassword();

        String response = model.performRequest("POST", username, password, "NULL_RECIPE", "login");
        int canLogin = Integer.parseInt(response);
        Stage stage = view2.getLoginWindowBody().getPrimaryStage();
        Scene scene = view2.getLoginWindowBody().getTargetScene();
        this.userSession = view2.getLoginWindowBody().getUserSession();
        RecipeList recipeList = view2.getLoginWindowBody().getRecipeList();
        this.recipeStorage = view2.getLoginWindowBody().getRecipeStorage();
        if (canLogin == 1) {
            stage.setScene(scene);
            this.userSession.setUsername(username);
            MongoDBClient mongoClient = new MongoDBClient(this.userSession.getUsername());
            try {
                mongoClient.openRecipes(recipeList, recipeStorage);
            } catch (Exception err) {
                
            }

        } else if (canLogin == 2) {
            view2.getLoginWindowBody().showAlert("Login Error", "Password Incorrect");
        } else if (canLogin == 3) {
            view2.getLoginWindowBody().showAlert("Login Error", "User Unknown");
        }
        view2.getLoginWindowBody().showAlert("Response: ", response);
    }

    private void handleClose(WindowEvent event) {
        String allRecipes = "";
        for (Recipe r : this.recipeStorage) {
            allRecipes += r.toString();
        }
        String username = this.userSession.getUsername();
        System.out.println(allRecipes);
        String response = model.performRequest("PUT", username, "NULL_PASSWORD", allRecipes, "close");
        try {
            userSession.clearSession();
        } catch (Exception ex) {
            
        }
    }
}