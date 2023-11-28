package project.Server;

import org.json.JSONObject;

public class MockChatAPI extends ChatAPI {

    public MockChatAPI(String instruction) {
        super(instruction);
    }

    @Override
    public String suggestRecipe() {
        // Log the JSON object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", this.instruction);
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 1.0);

        System.out.println(requestBody.toString());

        // For testing purposes, return a predefined JSON object
        return requestBody.toString();
    }
}
