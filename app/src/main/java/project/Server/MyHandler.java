package project.Server;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MyHandler implements HttpHandler {
    public MyHandler(){
    }
    
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
            }
            else{
                throw new Exception("Not Valid Request Method");
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


   

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response;
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        StringBuilder htmlBuilder = new StringBuilder();
        String name = query.substring(query.indexOf("=") + 1);
        htmlBuilder.append("<html>")
            .append("<body>")
            .append("<h1>")
            .append("Hello ")
            .append(name)
            .append("</h1>")
            .append("</body>")
            .append("</html>");
        response = htmlBuilder.toString();
        return response;
    }

}