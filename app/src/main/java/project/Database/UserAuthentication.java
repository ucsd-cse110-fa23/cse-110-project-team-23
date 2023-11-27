package project.Database;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.*;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class UserAuthentication {
    private static final String uri = "mongodb://asandoval2313:tTr2Pnu0ZiQ2pJuo@ac-hfcmrm5-shard-00-00.gixdies.mongodb.net:27017,ac-hfcmrm5-shard-00-01.gixdies.mongodb.net:27017,ac-hfcmrm5-shard-00-02.gixdies.mongodb.net:27017/?ssl=true&replicaSet=atlas-dzpzxt-shard-0&authSource=admin&retryWrites=true&w=majority";
    private String username;
    private String password;

    public UserAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean createAccount() {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase pantryPal_db = mongoClient.getDatabase("Database");

            // Check if the user already exists
            if (pantryPal_db.listCollectionNames().into(new ArrayList<>()).contains(username)) {
                System.out.println("User already exists. Choose a different username.");
                return false;
            } else {
                // Create a new collection for the user
                pantryPal_db.createCollection(username);

                // Insert user information into the user's collection
                Document document = new Document("username", username)
                        .append("password", encryptPassword(password));

                pantryPal_db.getCollection(username).insertOne(document);

                System.out.println("Account created successfully!");
                return true;
            }
        }
    }

    public int login() {
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase pantryPal_db = mongoClient.getDatabase("Database");
            MongoCollection<Document> userCollection = pantryPal_db.getCollection(username);
            if (userCollection != null) {
                // Fetch the document containing the user's information
                Document userDocument = userCollection.find().first();

                if (userDocument != null) {
                    // Retrieve the stored password
                    String storedPassword = userDocument.getString("password");

                    // Compare stored password with provided password
                    if (storedPassword.equals(encryptPassword(password))) {
                        System.out.println("Login successful!");
                        return 1;
                    } else {
                        System.out.println("Incorrect password!");
                        return 2;
                    }
                }
            }
        }

        return 3;
    }

    private String encryptPassword(String password) {
        // might need to encrypt password
        return password;
    }

}
