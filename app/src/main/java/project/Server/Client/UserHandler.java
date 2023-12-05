package project.Server.Client;
import com.sun.net.httpserver.*;

import project.Server.Recipe;
import project.Database.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Object;
public class UserHandler implements HttpHandler {
    private final Map<String, List<String>> users;
    
    public UserHandler(Map<String, List<String>> users){
        this.users = users;
    }
    
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

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();

        String username = postData.substring(0, postData.indexOf("!"));
        String password = postData.substring(postData.indexOf("!") + 1, postData.indexOf("@"));
        String isCreateAccount = postData.substring(postData.indexOf("@") + 1, postData.indexOf("#"));
        String verified = postData.substring(postData.indexOf("#")+1);

        ArrayList<String> data = new ArrayList<String>();
        data.add(password);
        data.add(isCreateAccount);
        data.add(verified);
        this.users.put(username, data);
        scanner.close();
        boolean canCreate = new UserAuthentication(username, password).createAccount();
    
        return Boolean.toString(canCreate);
    }
}