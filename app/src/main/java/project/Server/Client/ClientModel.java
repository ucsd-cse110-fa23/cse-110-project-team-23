package project.Server.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.*;

public class ClientModel {
    public String performRequest(String method, String username, String password, String recipes, String handler) {
        // Implement your HTTP request logic here and return the response

        try {
            String urlString = "http://localhost:8100/" + handler;

            String response = "nothing";
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //System.out.println(method);
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(username + "!" + password + "@");
                out.flush();
                out.close();
            }
            
            if (method.equals("PUT")){
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(username + "%");
                Scanner in = new Scanner(recipes);
                while (in.hasNextLine()) {
                    out.write(in.nextLine() + "&");
                }
                in.close();
                out.flush();
                out.close();
            }
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = in.readLine();
            
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error " + ex.getMessage();
        }
    }
}