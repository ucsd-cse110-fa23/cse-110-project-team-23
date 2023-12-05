package project.Server.Client;

import com.sun.net.httpserver.*;
import project.Database.*;

import java.io.*;
import java.util.*;

public class RecipeHandler implements HttpHandler {
    
    public RecipeHandler(){}
    
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received 123";
        String method = httpExchange.getRequestMethod();
        response += " " + method;
        try {
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
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

    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();

        String username = postData.substring(0, postData.indexOf("!"));

        scanner.close();
        
        String userRecipes = new MongoDBClient(username).recipesToString();
        //userSession.setUsername(username);
       // MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
        
        //System.out.println("Result" + recipes);
        return userRecipes;
    }
}
