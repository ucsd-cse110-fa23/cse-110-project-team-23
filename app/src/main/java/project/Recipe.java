package project;

public class Recipe {
    private String title;
    private String description;
    private String mealType;

    public Recipe(String title, String description, String mealType) {
        this.title = title;
        this.description = description;
        this.mealType = mealType;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMealType() {
        return this.mealType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMealType(String mealType) {
        if (mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner")) {
            this.mealType = mealType;
            return;
        }
    }
}