package project.Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatAPI {
        protected static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
        private static final String IMAGE_API_ENDPOINT = "https://api.openai.com/v1/images/generations";
        protected static final String API_KEY = "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";
        protected static final String CHAT_MODEL = "text-davinci-003";
        protected static final String IMAGE_MODEL = "dall-e-2";
        protected String instruction;

        public ChatAPI(String instruction) {
                this.instruction = instruction;
        }

        public String suggestRecipe() throws IOException, InterruptedException {
                // Set request parameters
                String prompt = "Given the following list of ingredients: " + this.instruction + //
                                "\nConstruct a response strictly in the following format:\n" + //
                                "Recipe Title: <Generated Title>\n" + //
                                "Meal Type: <Parsed Meal Type (i.e. breakfast, lunch, dinner)>\n" + //
                                "Recipe Instructions: <Generated Recipe Instructions (in a list)>";
                int maxTokens = 200;
                prompt = prompt.substring(0, prompt.length() - 1);

                // Create a request body which you will pass into request object
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", CHAT_MODEL);
                requestBody.put("prompt", prompt);
                requestBody.put("max_tokens", maxTokens);
                requestBody.put("temperature", 1.0);

                // Create the HTTP Client
                HttpClient client = HttpClient.newHttpClient();

                // Create the request object
                HttpRequest request = HttpRequest
                                .newBuilder()
                                .uri(URI.create(API_ENDPOINT))
                                .header("Content-Type", "application/json")
                                .header("Authorization", String.format("Bearer %s", API_KEY))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                                .build();

                // Send the request and receive the response
                HttpResponse<String> response = client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString());
                // Process the response
                String responseBody = response.body();

                JSONObject responseJson = new JSONObject(responseBody);

                JSONArray choices = responseJson.getJSONArray("choices");
                String generatedText = choices.getJSONObject(0).getString("text");

                return generatedText;
        }

        public String generateRecipeImage(String recipeTitle) throws IOException, InterruptedException {
                // Set request parameters
                String prompt = "Generate an image for the recipe with the title: " + recipeTitle;

                // Create a request body which you will pass into request object
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", IMAGE_MODEL);
                requestBody.put("prompt", prompt);
                requestBody.put("n", 1);
                requestBody.put("size", "256x256");

                // Create the HTTP client
                HttpClient client = HttpClient.newHttpClient();

                // Create the request object
                HttpRequest request = HttpRequest
                                .newBuilder()
                                .uri(URI.create("https://api.openai.com/v1/images/generations"))
                                .header("Content-Type", "application/json")
                                .header("Authorization", String.format("Bearer %s", API_KEY))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                                .build();

                // Send the request and receive the response
                HttpResponse<String> response = client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString());

                // Process the response
                String responseBody = response.body();

                JSONObject responseJson = new JSONObject(responseBody);
                JSONArray data = responseJson.getJSONArray("data");
                String generatedImageURL = data.getJSONObject(0).getString("url");

                return generatedImageURL;
        }
}