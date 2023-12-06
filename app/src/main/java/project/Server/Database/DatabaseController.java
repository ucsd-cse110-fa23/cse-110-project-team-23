/* 
package project.Server.Database;

import javafx.event.ActionEvent;
import project.Server.*;
import java.util.*;

public class DatabaseController {
    private View view;
    private Model model;

    public DatabaseController(View view, Model model) {
        this.view = view;
        this.model = model;
    }


    private boolean register() {
        //get from server
        String response = performRequest("GET");
        String username = response.parseUsername;
        String password = response.parsePassword;
        return MongoDBClient.createAccount(username, password);
        //create account
        //return value created or not
    }

     
    private void handlePostButton(ActionEvent event) {
        String language = view.getLanguage();
        String year = view.getYear();
        String response = model.performRequest("POST", language, year, null);
        view.showAlert("Response", response);
    }

    private void handleGetButton(ActionEvent event) {
        String query = view.getQuery();
        List<Recipe> recipeList = model.performRequest("GET", null, null, query);
        view.showAlert("Response", response);
    }

    private void handlePutButton(ActionEvent event) {
        String language = view.getLanguage();
        String year = view.getYear();
        String response = model.performRequest("PUT", language, year, null);
        view.showAlert("Response", response);
    }

    private void handleDeleteButton(ActionEvent event) {
        String query = view.getQuery();
        String response = model.performRequest("DELETE", null, null, query);
        view.showAlert("Response", response);
    }
    
}
*/