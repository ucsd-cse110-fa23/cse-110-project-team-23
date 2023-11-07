package test.PantryPal.java;

import org.junit.jupiter.api.Test;

import main.PantryPal.java.PantryPal;
import main.PantryPal.java.Recipe;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.Before;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PantryPalTests {
    @BeforeEach
    void setUp() {
        PantryPal.recipeStorage = new ArrayList<Recipe>();
        Recipe Recipe1 = new Recipe("Recipe", "Description");
        PantryPal.recipeStorage.add(Recipe1);

    }   

 
    @Test
    void testDivide() {
        assertThrows(ArithmeticException.class, () -> {
            Integer.divideUnsigned(42, 2);
        });
    }   

   
    @Test
    void testAddRecipe() {
        int FirstSize = PantryPal.recipeStorage.size();
        String Recipe = "Recipe";
        String RecipeDescription = "description";
        PantryPal.recipeStorage.add(new Recipe(Recipe, RecipeDescription));

        boolean recipeAdded = false;
        for (int i = 0; i < PantryPal.recipeStorage.size(); i++) {
            Recipe recipe = PantryPal.recipeStorage.get(i);
            if (recipe.getTitle().equals(Recipe) && recipe.getDescription().equals(RecipeDescription)) {
                recipeAdded = true;
            }
        }
        int finalSize = PantryPal.recipeStorage.size();

        assertTrue(recipeAdded, "");
        assertEquals(FirstSize + 1, finalSize, "");
    }
}
