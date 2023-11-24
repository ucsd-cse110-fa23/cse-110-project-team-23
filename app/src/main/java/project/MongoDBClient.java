package project;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.*;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class MongoDBClient {
    private static final String uri = "mongodb://asandoval2313:tTr2Pnu0ZiQ2pJuo@ac-hfcmrm5-shard-00-00.gixdies.mongodb.net:27017,ac-hfcmrm5-shard-00-01.gixdies.mongodb.net:27017,ac-hfcmrm5-shard-00-02.gixdies.mongodb.net:27017/?ssl=true&replicaSet=atlas-dzpzxt-shard-0&authSource=admin&retryWrites=true&w=majority";
    private String username;

    public MongoDBClient(String username) {
        this.username = username;
    }

    public void insertRecipe(String title, String mealType, String description) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase pantryPal_db = mongoClient.getDatabase("Database");
            MongoCollection<Document> userCollection = pantryPal_db.getCollection(username);

            Document recipeDoc = new Document("_id", new ObjectId());
            recipeDoc.append("title", title)
                    .append("mealType", mealType)
                    .append("description", description);
            userCollection.insertOne(recipeDoc);
        }
    }

    public void deleteRecipe(String title) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase pantryPal_db = mongoClient.getDatabase("Database");
            MongoCollection<Document> userCollection = pantryPal_db.getCollection(username);
            Bson filter = eq("title", title);
            userCollection.deleteOne(filter);
        }
    }

    public void openRecipes(RecipeList recipeList) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase pantryPalDb = mongoClient.getDatabase("Database");

            // Fetch the collection with the username
            MongoCollection<Document> userCollection = pantryPalDb.getCollection(username);

            // Fetch all documents in the collection
            List<Document> recipes = userCollection.find().into(new ArrayList<>());

            // Iterate through documents, skipping the first one
            for (int i = 1; i < recipes.size(); i++) {
                Document recipeDoc = recipes.get(i);
                String title = recipeDoc.getString("title");
                String description = recipeDoc.getString("description");
                String mealType = recipeDoc.getString("mealType");

                // Create and add a new Recipe object to the recipeList
                Recipe newRecipe = new Recipe(title, description, mealType);
                recipeList.addRecipe(title);
            }
        }
    }
}