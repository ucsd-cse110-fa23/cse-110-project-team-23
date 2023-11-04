import javax.swing.border.Border;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.*;

class Recipe extends HBox{
    private Label index;
    private Label recipeName;
    private TextField title;
    Recipe() {
        this.setPrefSize(500, 50);


        //Title
        title = new TextField();
    }

}

class RecipeList<T> extends VBox {
    Scene scene1;
    RecipeList() {
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void addrecipe(){
        this.getChildren().add(new Recipe());

    }
}

class Header extends HBox {
    private Button addButton;
    private Stage primaryStage = new Stage();
    StackPane layout1 = new StackPane();
    Scene scene1 = new Scene(layout1, 300,300);
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addButton = new Button("Add Recipe");
        addButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton);
        addListeners();
    }

    public Button getAddButton() {
        return addButton;
    }
    
    public void addListeners(){
        Button addButton = this.getAddButton();
        addButton.setOnAction(e1 -> primaryStage.setScene(scene1));
    }
    
}
   
class Footer extends HBox {
    Footer() {
        this.setPrefSize(500, 0);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
}

class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private ScrollPane scrollPane;
    

    private Button addButton;
    AppFrame() {
        header = new Header();
        footer = new Footer();
        recipeList = new RecipeList();

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollPane);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        

   
    }

    public Header getheader(){
        return header;
    }
     
}



class addRecipeInfo extends BorderPane{
    private TextField title;
    private TextField description;
    private Label title1;
    private HBox header1;

    addRecipeInfo(){
        header1 =  new HBox();
        title1 = new Label();
        title1.setText("Title");
        title1.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font: 20 arial;");
        title1.setPrefSize(100, 30);
        title1.setPadding(new Insets(35, 0, 20, 20));
        header1.getChildren().add(title1);
        title = new TextField();
        title.setPrefSize(400,50);
        title.setStyle("-fx-font-style: italic; -fx-background-color: #F5F4F4;  -fx-font-weight: bold; -fx-font: 20 arial;");
        title.setPadding(new Insets(35, 0, 20, 0));
        header1.getChildren().add(title);
        description = new TextField();
        description.setPrefSize(200,300);
        description.setPromptText("Description");
        this.setTop(header1);
        this.setCenter(description);
    }

}
public class PantryPal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
         addRecipeInfo layout1 = new addRecipeInfo();
         Scene scene1 = new Scene(layout1, 500,400);
        // Setting the Layout of the Window (Flow Pane)
        AppFrame root = new AppFrame();
        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 400));

        Button addButton = root.getheader().getAddButton();
        addButton.setOnAction(e -> primaryStage.setScene(scene1));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
