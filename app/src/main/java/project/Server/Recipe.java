package project.Server;

public class Recipe {
    private String title;
    private String description;
    private String mealType;
    private String imageURL;

    public Recipe(String title, String description, String mealType, String imageURL) {
        this.title = title;
        this.description = description;
        this.mealType = mealType;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return this.imageURL;
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

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String toString() {
        return this.getTitle() + "!" + this.getDescription() + "@" + this.getMealType() + "$" + "FakeURL" + "#";
    }
}