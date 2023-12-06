package project.Server.Client;

import com.sun.net.httpserver.*;
import project.Database.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class ShareHandler implements HttpHandler {
    private Map<String,Map<String,String>> data; 
    public ShareHandler(Map<String, Map<String, String>> data){ this.data = data;}
    
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received 123";
        String method = httpExchange.getRequestMethod();
        response += " " + method;
        try {
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
            }
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
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
        while (scanner.hasNextLine()) {
            postData += scanner.nextLine();
        }
        
        String username = postData.substring(0, postData.indexOf("!"));
        String title = postData.substring(postData.indexOf("!") + 1, postData.indexOf("@"));
        String recipe = postData.substring(postData.indexOf("@") + 1);

        Map<String, String> user = this.data.get(username);
        if (user != null) {
            user.put(title, recipe);
        } else {
            Map<String, String> temp = new HashMap<String, String>();
            temp.put(title, recipe);
            this.data.put(username, temp);
        }
        scanner.close();
        
        return recipe;
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response;
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        StringBuilder htmlBuilder = new StringBuilder();
        String username = query.substring(query.indexOf("=") + 1, query.indexOf("/"));
        String title = query.substring(query.indexOf('/') + 1);
        Map<String,String> recipes = this.data.get(username);
        String recipe = recipes.get(title);
        String temp = "";
        while(recipe.indexOf("&") != -1) {
            temp += recipe.substring(0, recipe.indexOf("&"));
            recipe = recipe.substring(recipe.indexOf("&") + 1);
        }
        temp += recipe;
        htmlBuilder.append("<html>")
            .append("<body>")
            .append("<h1>")
            .append(title)
            .append("</h1>")
            .append("<p>")
            .append(temp)
            .append("</p>")
            .append("</body>")
            .append("</html>");
        response = htmlBuilder.toString();
        return response;
    }
}
