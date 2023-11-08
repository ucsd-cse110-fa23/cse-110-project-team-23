package project;

public class Recipe {
    private String title;
    private String description;

    public Recipe(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public void setDescription(String _description) {
        description = _description;
    }
}