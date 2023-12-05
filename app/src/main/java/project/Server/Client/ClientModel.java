package project.Server.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;


public class ClientModel {
    public String performRequest(String method, String username, String password, String isCreateAccount, String verified, String handler) {
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
                out.write(username + "!" + password + "@" + isCreateAccount + "#" + verified);
                out.flush();
                out.close();
            }
            
            // if (method.equals("GET")) {
            //     BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //     response = in.readLine();
            //     in.close();
            // }
            
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