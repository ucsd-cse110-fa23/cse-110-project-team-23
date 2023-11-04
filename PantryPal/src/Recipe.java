public class Recipe {
    private String Title;
    private String description;


    public Recipe (String Title, String description){
        this.Title = Title;
        this.description = description;
    }

    public String getTitle(String Title){
        return this.Title;
    }


    public String getDescription(String description){
        return this.description;
    }

    
}
