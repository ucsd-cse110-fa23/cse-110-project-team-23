package project.Server;

import org.json.JSONObject;

public class MockChatAPI extends ChatAPI {

    protected boolean recipeRefreshed = false;

    public MockChatAPI(String instruction) {
        super(instruction);
    }


    @Override
    public String suggestRecipe() {
        

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", this.instruction);
        requestBody.put("max_tokens", 200);
        
        double temperature;
        int maxtokens;

        if(recipeRefreshed == true){
            temperature = 0.9;
            maxtokens = 100;
        }else{
            temperature =1.0;
            maxtokens = 200;
        }
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxtokens);
        String result = requestBody.toString();
        recipeRefreshed = true;
        return result;
    }

}
