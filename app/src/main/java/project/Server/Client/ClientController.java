package project.Server.Client;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.Client.CreateAccountWindow.*;
import project.Client.LoginWindow.*;
import project.Client.MainWindow.*;
import project.Database.*;
import project.Server.Recipe;

import java.util.*;

public class ClientController {
    private CreateAccountWindow view1;
    private LoginWindow view2;
    private ClientModel model;

    public ClientController(CreateAccountWindow view1, LoginWindow view2, ClientModel model) {
        this.view1 = view1;
        this.view2 = view2;
        this.model = model;

        this.view1.getCreateAccountBody().createAccountButtonAction(this::handleCreateAccountButton);
        this.view2.getLoginWindowBody().loginButtonAction(this::handleLoginButton);
    }

    private void handleCreateAccountButton(ActionEvent event) {
        String username = view1.getCreateAccountBody().getUsername();
        String password = view1.getCreateAccountBody().getPassword();
        
        System.out.println("reached");
        


        String response = model.performRequest("POST", username, password, "true", "false","create");
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

        String response = model.performRequest("POST", username, password, "false", "false", "login");
        int canLogin = Integer.parseInt(response);
        Stage stage = view2.getLoginWindowBody().getPrimaryStage();
        Scene scene = view2.getLoginWindowBody().getTargetScene();
        UserSession userSession = view2.getLoginWindowBody().getUserSession();

        if (canLogin == 1) {
            stage.setScene(scene);
            userSession.setUsername(username);
            MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
        } else if (canLogin == 2) {
            view2.getLoginWindowBody().showAlert("Login Error", "Password Incorrect");
        } else if (canLogin == 3) {
            view2.getLoginWindowBody().showAlert("Login Error", "User Unknown");
        }
        view2.getLoginWindowBody().showAlert("Response: ", response);
        
        String response1 = model.performRequest("POST", username, password, "false", "false", "recipe");

        RecipeList recipeList = view2.getLoginWindowBody().getRecipeList();
        List<Recipe> recipeStorage = view2.getLoginWindowBody().getRecipeStorage();

        while (response1.indexOf("$") != -1) {
            String line = response1.substring(0, response1.indexOf("$"));
            String title = line.substring(0, line.indexOf("!"));
            String description = line.substring(line.indexOf("!") + 1, line.indexOf("@"));
            String mealType = line.substring(line.indexOf("@") + 1, line.indexOf("#"));
            String imageURL = line.substring(line.indexOf("#") + 1);
            Recipe reicpe = new Recipe(title, description, mealType, imageURL);
            System.out.println(title);
            recipeStorage.add(reicpe);
            recipeList.addRecipe(title, imageURL);
            response1 = response1.substring(response1.indexOf("$"));
        }
    }
}