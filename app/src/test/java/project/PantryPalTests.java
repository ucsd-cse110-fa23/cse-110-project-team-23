package project;

import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class PantryPalTests {
    String title;
    String description = "Raw";
    String mealType = "Breakfast";
    int startSize;
    PantryPal pantryPal;
    private static final String AUDIO_FILE_PATH = "recording.wav";

    @BeforeEach
    void setUp() {
        startSize = 0;
        title = "Chicken";
        description = "Raw";
        mealType = "Breakfast";
        PantryPal.recipeStorage = new ArrayList<>();
        Recipe testRecipe = new Recipe(title, description, mealType);
        PantryPal.recipeStorage.add(testRecipe);
    }

    @Test
    void testAddRecipe() {
        boolean recipeAdded = false;
        for (int i = 0; i < PantryPal.recipeStorage.size(); i++) {
            Recipe recipe = PantryPal.recipeStorage.get(i);
            if (recipe.getTitle().equals(title)
                    && recipe.getDescription().equals(description)
                    && recipe.getMealType().equals(mealType)) {
                recipeAdded = true;
            }
        }

        int finalSize = PantryPal.recipeStorage.size();

        assertTrue(recipeAdded, "");
        assertEquals(startSize + 1, finalSize, "");
    }

    @Test
    void testSetMealtype() {
        Recipe recipe = new Recipe(null, null, null);
        recipe.setMealType("Lunch");

        assertEquals("Lunch", recipe.getMealType());
    }

    @Test
    void testViewRecipe() {
        Recipe recipe = PantryPal.recipeStorage.get(0);
        String mealtype1 = recipe.getMealType();
        String description1 = recipe.getDescription();
        String title1 = recipe.getTitle();

        assertEquals(title1, title);
        assertEquals(description1, description);
        assertEquals(mealtype1, mealType);
    }

    @Test
    void testEditRecipe() {
        Recipe recipe = PantryPal.recipeStorage.get(0);
        recipe.setDescription("null");
        recipe.setMealType("brunch");
        recipe.setTitle("nothing");

        assertEquals("null", recipe.getDescription());
        assertEquals("Breakfast", recipe.getMealType());
        assertEquals("nothing", recipe.getTitle());
    }

    @Test
    void testDeleteRecipe() {
        PantryPal.recipeStorage.remove(0);

        assertEquals(0, PantryPal.recipeStorage.size());
    }

    @Test
    void testSaveRecipe() {
        Recipe recipe1 = new Recipe("orange chicken", "orange,chicken", "Lunch");

        PantryPal.recipeStorage.add(recipe1);
        assertEquals(recipe1, PantryPal.recipeStorage.get(1));
    }

    @Test
    void testChatGpt() {
        try {
            ChatAPI chat = new ChatAPI(description);
            Recipe recipe2 = new Recipe("", "", "");
            recipe2.setDescription(chat.suggestRecipe());

            assertNotEquals("", recipe2.getDescription());
        } catch (Exception e1) {

        }

    }

    // @Test
    // void testTranscribeAudio() throws InterruptedException {
    // Platform.startup(() -> {
    // });
    // TextField textField = new TextField();

    // WhisperAPI whisperAPI = new WhisperAPI(AUDIO_FILE_PATH, textField);

    // whisperAPI.transcribeAudio();
    // Thread.sleep(5000);

    // String transcribedText = textField.getText();
    // assertNotNull(transcribedText);
    // assertFalse(transcribedText.isEmpty());
    // }

}