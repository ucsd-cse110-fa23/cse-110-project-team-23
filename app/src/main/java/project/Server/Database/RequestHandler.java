/* 
package project.Server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class RequestHandler implements HttpHandler {
	private final Map<String,String> data;
	private final Map<String, List<Recipe>> data;
	public RequestHandler(Map<String, List<Recipe>> data) {
		this.data = data;
	}


	public void handle(HttpExchange httpExchange) throws IOException {
		List<Recipe> recipeList;
		String method = httpExchange.getRequestMethod();

		try {
			if (method.equals("GET")) {
				recipeList = handleGet(httpExchange);	
			} 
			 
			else if (method.equals("POST")) {
				response = handlePost(httpExchange);
			} else if (method.equals("PUT")){
				response = handlePut(httpExchange);
			} else if (method.equals("DELETE")){
				response = handleDelete(httpExchange);
			} else{
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

	private List<Recipe> handleGet(HttpExchange httpExchange) throws IOException {
		URI uri = httpExchange.getRequestURI();
		String query = uri.getRawQuery();
		if (query != null) {
			String username = query.substring(query.indexOf("=") + 1);
			List<Recipe> recipeList = data.get(username);
			return recipeList;
		}
		return null;
	}
	/* 
	private String handlePost(HttpExchange httpExchange) throws IOException {
		InputStream inStream = httpExchange.getRequestBody();
		Scanner scanner = new Scanner(inStream);
		String postData = scanner.nextLine();
		String language = postData.substring(
		0, postData.indexOf(",")), year = postData.substring(postData.indexOf(",") + 1);

		// Store data in hashmap
		data.put(language, year);
		String response = "Posted entry {" + language + ", " + year + "}";
		System.out.println(response);
		scanner.close();

		return response;
	}

	private String handlePut(HttpExchange httpExchange) throws IOException{
		InputStream inStream = httpExchange.getRequestBody();
		String response;
		Scanner scanner = new Scanner(inStream);
		String postData = scanner.nextLine();
		String language = postData.substring(
			0,
			postData.indexOf(",")
		), year = postData.substring(postData.indexOf(",") + 1);

		if (data.get(language) != null){
			data.replace(language,year);
			response = "Updated {" + language + ", " + year + "}";
			System.out.println(response);
			scanner.close();
		}
		else{
			data.put(language,year);
			response = "Added entry {" +  language + "," + year + "}";
			System.out.println(response);
			scanner.close();
		}
		return response;
	}

	private String handleDelete(HttpExchange httpExchange) throws IOException{
		String response;
		URI uri = httpExchange.getRequestURI();
		String query = uri.getRawQuery();
		String language = query.substring(query.indexOf("=") + 1);
		if (data.get(language) != null) {
			String year = data.get(language);
			data.remove(language);
			response = "Deleted entry " + "{" + language + ", " + year + "}";
		}
		else{
			response = "No data found for " + language;
		}
		return response;
  	}
}
*/