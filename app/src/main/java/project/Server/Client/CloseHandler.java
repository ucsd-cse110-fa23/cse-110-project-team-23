package project.Server.Client;
import com.sun.net.httpserver.*;

import project.Server.Recipe;
import project.Database.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Object;
public class CloseHandler implements HttpHandler {
    
    public CloseHandler(){}
    
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received 123";
        String method = httpExchange.getRequestMethod();
        response += " " + method;
        try {
            if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine();
        }
        String username = postData.substring(0, postData.indexOf("%"));
        String recipes = postData.substring(postData.indexOf("%") + 1);
        MongoDBClient database = new MongoDBClient(username);
        database.deleteAll();
        //database.insertRecipe("" + recipes, "breakfast", "something", "url");
        while (recipes.indexOf("#") != -1) {
            String title = recipes.substring(0, recipes.indexOf("!"));
            String description = recipes.substring(recipes.indexOf("!") + 1, recipes.indexOf("@"));
            String mealType = recipes.substring(recipes.indexOf("@") + 1, recipes.indexOf("$"));
            String imageURL = recipes.substring(recipes.indexOf("$") + 1, recipes.indexOf("#"));
            database.insertRecipe(title, mealType, description, imageURL);
            recipes = recipes.substring(recipes.indexOf("#") + 1);
        }
        scanner.close();
        return "Success";
    }
}
