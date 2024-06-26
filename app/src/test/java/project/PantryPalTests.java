package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project.Client.MainWindow.MainWindowHeader;
import project.Database.MockUserAuthentication;
import project.Server.ChatAPI;
import project.Server.MockChatAPI;
import project.Server.Recipe;

public class PantryPalTests {
    String title;
    String description = "Raw";
    String mealType = "Breakfast";
    String imageURL;
    String imageURL2 ="initial_image_url";
    int startSize;
    PantryPal pantryPal;
    ArrayList<Recipe> mockRecipeStorage;
    ArrayList<Recipe> alphaSort;
    ArrayList<Recipe> oldSort;
    ArrayList<Recipe> newSort;
    ArrayList<Recipe> breakfast;
    ArrayList<Recipe> Lunch;
    ArrayList<Recipe> Dinner;

    ArrayList<Recipe> mockRecipeStorage2 = new ArrayList<>();
    private static final String AUDIO_FILE_PATH = "recording.wav";

    @BeforeEach
    void setUp() { 
        startSize = 0;
        title = "Chicken";
        description = "Raw";
        mealType = "Breakfast";
        imageURL = "";
        PantryPal.recipeStorage = new ArrayList<>();
        Recipe testRecipe = new Recipe(title, description, mealType, imageURL);
        PantryPal.recipeStorage.add(testRecipe);

        mockRecipeStorage = new ArrayList<>();
        Recipe sortRecipe1 = new Recipe("Chicken", "Cooked chicken", "Dinner", imageURL);
        Recipe sortRecipe2 = new Recipe("Orange chicken", "null", "Dinner", imageURL);
        Recipe sortRecipe3 = new Recipe("Apple", "raw", "Lunch", imageURL);
        mockRecipeStorage.add(sortRecipe1);
        mockRecipeStorage.add(sortRecipe2);
        mockRecipeStorage.add(sortRecipe3);
        alphaSort = new ArrayList<Recipe>();
        oldSort = new ArrayList<Recipe>();
        newSort = new ArrayList<Recipe>();
        alphaSort.add(sortRecipe3);
        alphaSort.add(sortRecipe1);
        alphaSort.add(sortRecipe2);
        oldSort.add(sortRecipe1);
        oldSort.add(sortRecipe2);
        oldSort.add(sortRecipe3);
        newSort.add(sortRecipe3);
        newSort.add(sortRecipe2);
        newSort.add(sortRecipe1);

        mockRecipeStorage2 = new ArrayList<>();
        Recipe Recipe1 = new Recipe("Chicken", "Cooked chicken", "Breakfast", imageURL);
        Recipe Recipe2 = new Recipe("Orange chicken", "null", "Dinner", imageURL);
        Recipe Recipe3 = new Recipe("Apple", "raw", "Lunch", imageURL);
        mockRecipeStorage2.add(Recipe1);
        mockRecipeStorage2.add(Recipe2);
        mockRecipeStorage2.add(Recipe3);
        breakfast = new ArrayList<Recipe>();
        Lunch = new ArrayList<Recipe>();
        Dinner = new ArrayList<Recipe>();
        breakfast.add(Recipe1);
        Lunch.add(Recipe3);;
        Dinner.add(Recipe2);
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
        Recipe recipe = new Recipe(null, null, null, null);
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
        Recipe recipe1 = new Recipe("orange chicken", "orange,chicken", "Lunch", imageURL);

        PantryPal.recipeStorage.add(recipe1);
        assertEquals(recipe1, PantryPal.recipeStorage.get(1));
    }

    @Test
    void testChatGpt() {
        try {
            ChatAPI chat = new ChatAPI(description);
            Recipe recipe2 = new Recipe("", "", "", imageURL);
            recipe2.setDescription(chat.suggestRecipe());

            assertNotEquals("", recipe2.getDescription());
        } catch (Exception e1) {

        }

    }

    @Test
    public void testCreateAccount() {
        MockUserAuthentication mockAuth = new MockUserAuthentication("testUser", "testPassword");
        assertTrue(mockAuth.createAccount());
    }

    @Test
    public void testCreateAccountAlreadyExists() {
        MockUserAuthentication mockAuth = new MockUserAuthentication(MockUserAuthentication.MOCK_USERNAME,
                MockUserAuthentication.MOCK_PASSWORD);
        assertFalse(mockAuth.createAccount());
    }

    @Test
    public void testLoginSuccessful() {
        MockUserAuthentication mockAuth = new MockUserAuthentication(MockUserAuthentication.MOCK_USERNAME,
                MockUserAuthentication.MOCK_PASSWORD);
        assertEquals("Mock Login successful!", mockAuth.login());
    }

    @Test
    public void testLoginFailed() {
        MockUserAuthentication mockAuth = new MockUserAuthentication("wrongUser", "wrongPassword");
        assertEquals("Mock Incorrect username/password!", mockAuth.login());
    }

    @Test 
    public void testServerIsOFFwhenLogin(){
         MockUserAuthentication mockAuth = new MockUserAuthentication("", "");
        assertEquals("Server is unavailable!", mockAuth.login());
    }

    @Test 
    public void testServerIsOFFwhenCreateAccount(){
         MockUserAuthentication mockAuth = new MockUserAuthentication("", "");
        assertFalse(mockAuth.createAccount());
    }
    @Test
    public void testSuggestRecipe() {

        String instruction = "test ingredients";
        MockChatAPI mockChatAPI = new MockChatAPI(instruction);

        String result = mockChatAPI.suggestRecipe();

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("model", "text-davinci-003");
        expectedJson.put("prompt", "test ingredients");
        expectedJson.put("max_tokens", 200);
        expectedJson.put("temperature", 1.0);
        String expectedString = expectedJson.toString();

        assertEquals(expectedString, result);
    }

    @Test
    public void refreshrecipe() {

        String initialInstruction = "test ingredients";
        MockChatAPI mockChatAPI = new MockChatAPI(initialInstruction);

        String initialResult = mockChatAPI.suggestRecipe();

        JSONObject expectedInitialJson = new JSONObject();
        expectedInitialJson.put("model", "text-davinci-003");
        expectedInitialJson.put("prompt", "test ingredients");
        expectedInitialJson.put("max_tokens", 200);
        expectedInitialJson.put("temperature", 1.0);
        String expectedInitialString = expectedInitialJson.toString();

        assertEquals(expectedInitialString, initialResult);

        String refreshedResult = mockChatAPI.suggestRecipe();

        assertNotEquals(initialResult, refreshedResult);
    }

    @Test
    public void testSortAlpha() {
        ArrayList<Recipe> afterSort = MainWindowHeader.sortAlpha(mockRecipeStorage);
        assertEquals(alphaSort, afterSort);
    }

    @Test
    public void testSortNew() {
        ArrayList<Recipe> afterSort = MainWindowHeader.sortNew(mockRecipeStorage);
        assertEquals(newSort, afterSort);
    }



    @Test
    public void testSortOld() {
        ArrayList<Recipe> afterSort = MainWindowHeader.sortOld(mockRecipeStorage);
        assertEquals(oldSort, afterSort);
    }

        @Test
    public void testFilter1() {
        ArrayList<Recipe> afterSort = MainWindowHeader.FilterbyBreakfast(mockRecipeStorage2);
        assertEquals(breakfast, afterSort);
    }

        @Test
    public void testFilter2() {
        ArrayList<Recipe> afterSort = MainWindowHeader.FilterbyLunch(mockRecipeStorage2);
        assertEquals(Lunch, afterSort);
    }

    @Test
    public void testFilter3() {
        ArrayList<Recipe> afterSort = MainWindowHeader.FilterbyDinner(mockRecipeStorage2);
        assertEquals(Dinner, afterSort);
    }

    @Test
    public void testImage(){
        Recipe Recipe1 = new Recipe("Chicken", "Cooked chicken", "Breakfast",imageURL2);

        assertEquals("initial_image_url", Recipe1.getImageURL());
        Recipe1.setImageURL("new_image_url");

        assertEquals("new_image_url", Recipe1.getImageURL());
    }

    @Test
    void integrationTest() {
        MockUserAuthentication mockAuth = new MockUserAuthentication("testUser", "testPassword");
        assertTrue(mockAuth.createAccount());

        MockUserAuthentication mockAuthLogin = new MockUserAuthentication("mockUser", "mockPassword");
        assertEquals("Mock Login successful!", mockAuthLogin.login());

        Recipe recipe1 = new Recipe("Chicken", "Cooked chicken", "Dinner", "");
        Recipe recipe2 = new Recipe("Orange chicken", "null", "Dinner", "");
        Recipe recipe3 = new Recipe("Apple", "raw", "Lunch", "");
        Recipe recipe4 = new Recipe("Test Recipe", "Test description", "Breakfast", "");

        PantryPal.recipeStorage = new ArrayList<>();
        PantryPal.recipeStorage.add(recipe1);
        PantryPal.recipeStorage.add(recipe2);
        PantryPal.recipeStorage.add(recipe3);
        PantryPal.recipeStorage.add(recipe4);

        ArrayList<Recipe> alphaSort = MainWindowHeader.sortAlpha(PantryPal.recipeStorage);

        assertEquals(recipe3, alphaSort.get(0));
        assertEquals(recipe1, alphaSort.get(1));
        assertEquals(recipe2, alphaSort.get(2));
        assertEquals(recipe4, alphaSort.get(3));

        ArrayList<Recipe> breakfastFilter = MainWindowHeader.FilterbyBreakfast(PantryPal.recipeStorage);
        ArrayList<Recipe> lunchFilter = MainWindowHeader.FilterbyLunch(PantryPal.recipeStorage);
        ArrayList<Recipe> dinnerFilter = MainWindowHeader.FilterbyDinner(PantryPal.recipeStorage);

        assertTrue(breakfastFilter.contains(recipe4));
        assertTrue(lunchFilter.contains(recipe3));
        assertTrue(dinnerFilter.contains(recipe1));
        assertTrue(dinnerFilter.contains(recipe2));
        assertFalse(breakfastFilter.contains(recipe1));
        assertFalse(lunchFilter.contains(recipe2));
        assertFalse(dinnerFilter.contains(recipe3));
        assertFalse(breakfastFilter.contains(recipe3));
    }

}
