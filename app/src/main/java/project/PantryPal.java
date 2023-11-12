package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;

import java.io.IOException;
import java.util.*;

/**
 * References:
 * https://stackoverflow.com/questions/55389052/how-to-set-javafx-textfield-to-wrap-text-inside-field
 * 
 */

/**
 * A Recipe in RecipeList (App display)
 */
class RecipeBox extends HBox {
    private Button title;
    private String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    private Button deleteButton;

    RecipeBox(String title) {
        this.setPrefSize(500, 50);

        // Set recipe appearance
        this.title = new Button();
        this.title.setPrefSize(500, 50);
        this.title.setText(title);
        this.title.setStyle(defaultButtonStyle);
        this.getChildren().add(this.title);

        // Set up delete button apperance
        this.deleteButton = new Button("delete");
        this.deleteButton.setPrefSize(150, 100);

        // deletes the recipe from the list
        this.deleteButton.setOnAction(e -> {
            Recipe Rp = getRecipeByTitle(this.title.getText());
            PantryPal.recipeStorage.remove(Rp);
            try {
                RecipeList List = (RecipeList) getParent();
                List.getChildren().remove(this);
            } catch (NullPointerException error) {
            }
            ;
        });
        // adds the delete button
        this.getChildren().add(this.deleteButton);

        // Creates a new RecipeDetailsView window using selected recipe
        this.title.setOnAction(e -> {
            Recipe recipe = getRecipeByTitle(title);
            if (recipe != null) {
                RecipeDetailsView recipeDetailsView = new RecipeDetailsView(recipe, this.getScene());
                Scene recipeDetailsScene = new Scene(recipeDetailsView, 500, 400);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setScene(recipeDetailsScene);
            }
        });
    }

    private Recipe getRecipeByTitle(String title) {
        for (Recipe recipe : PantryPal.recipeStorage) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }
        return null;
    }
}

/**
 * The main recipe list displayed in app.
 */
class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5); // sets spacing between recipes
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    // Adds a single recipe to the main list given the title of recipe
    public void addRecipe(String title) {
        this.getChildren().add(0, new RecipeBox(title)); // add new recipe to top of list
    }
}

/**
 * Header segment of main window, displays Recipe List and the AddRecipe button
 */
class MainWindowHeader extends HBox {

    private Button addRecipeButton;

    MainWindowHeader() {
        // Set Header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        // Add Recipe button
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        addRecipeButton = new Button("Add Recipe");
        addRecipeButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(titleText, addRecipeButton);
    }

    public Button getAddButton() {
        return addRecipeButton;
    }

}

/**
 * Main window layout
 */
class MainWindow extends BorderPane {
    private MainWindowHeader mainWindowHeader; // header section
    private RecipeList recipeList;
    private ScrollPane scrollPane;

    MainWindow() {
        mainWindowHeader = new MainWindowHeader();
        recipeList = new RecipeList();

        scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(mainWindowHeader);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scrollPane);
    }

    public MainWindowHeader getHeader() {
        return mainWindowHeader;
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }
}

/**
 * Header segment for SuggestWindow
 */
class SuggestWindowHeader extends HBox {
    private Button returnButton;

    SuggestWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Generate Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(titleText, returnButton);
    }

    public Button getReturnButton() {
        return returnButton;
    }
}

/**
 * Center segment for SuggestWindow
 */
class SuggestWindowBody extends VBox {
    private Label ingredientLabel;
    private TextField ingredientTextField;
    private Button recordIngredientsButton;
    private Button confirmButton;
    private Label recordingLabel;
    private Label emptyLabel;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private boolean recording = false;

    private static final String FILE_PATH = "/home/adsandov/code/pantrypal/cse-110-project-team-23/app/recording.wav";

    SuggestWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        ingredientLabel = new Label();
        ingredientLabel.setText("Ingredients");
        ingredientLabel.setPrefSize(100, 30);

        ingredientTextField = new TextField();
        ingredientTextField.setPrefSize(400, 50);

        recordIngredientsButton = new Button("Record Ingredients");
        confirmButton = new Button("Confirm Ingredients");
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font: 11 arial;";
        recordIngredientsButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(
                "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden");

        emptyLabel = new Label("Please Record or Type Ingredients");
        emptyLabel.setStyle(
                "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden");

        recordIngredientsButton.setOnAction(e -> {
            if (!recording) {
                recording = true;
                recordIngredientsButton.setText("Stop");
                startRecording();
            } else {
                recording = false;
                recordIngredientsButton.setText("Record Ingredients");
                stopRecording();
                ingredientTextField.setText("transcribing...");
                WhisperAPI whisper = new WhisperAPI(FILE_PATH, ingredientTextField);
                whisper.transcribeAudio();
            }
        });

        confirmButton.setStyle(defaultButtonStyle);
        confirmButton.setOnAction(e -> {
            if (recording) {
                recording = false;
                recordIngredientsButton.setText("Record Ingredients");
                stopRecording();
                recordingLabel.setVisible(false);
            }

            String ingredients = ingredientTextField.getText();
            try {
                ChatAPI chatAPI = new ChatAPI(ingredients);
                String suggestion = chatAPI.suggestRecipe();
                System.out.println("Generated Recipe Suggestion:\n" + suggestion);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        // Add buttons to the layout
        HBox buttonBox = new HBox(recordIngredientsButton, confirmButton);
        buttonBox.setSpacing(10);

        audioFormat = getAudioFormat();

        this.getChildren().addAll(ingredientLabel, ingredientTextField, buttonBox,
                recordingLabel);
    }

    public String getIngredients() {
        return ingredientTextField.getText();
    }

    public void clear() {
        ingredientTextField.clear();
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 2;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // the format of the TargetDataLine
                            DataLine.Info dataLineInfo = new DataLine.Info(
                                    TargetDataLine.class,
                                    audioFormat);
                            // the TargetDataLine used to capture audio data from the microphone
                            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                            targetDataLine.open(audioFormat);
                            targetDataLine.start();
                            recordingLabel.setVisible(true);
                            emptyLabel.setVisible(false);

                            // the AudioInputStream that will be used to write the audio data to a file
                            AudioInputStream audioInputStream = new AudioInputStream(
                                    targetDataLine);

                            // the file that will contain the audio data
                            File audioFile = new File("recording.wav");
                            AudioSystem.write(
                                    audioInputStream,
                                    AudioFileFormat.Type.WAVE,
                                    audioFile);
                            recordingLabel.setVisible(false);
                            Thread.sleep(5 * 1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }

}

/**
 * Suggest window layout
 */
class SuggestWindow extends BorderPane {
    private SuggestWindowHeader header;
    private SuggestWindowBody body;

    SuggestWindow() {
        header = new SuggestWindowHeader();
        body = new SuggestWindowBody();

        this.setTop(header);
        this.setCenter(body);
        // Add other components for the footer as needed.
    }

    public SuggestWindowHeader getSuggestWindowHeader() {
        return header;
    }

    public SuggestWindowBody getSuggestWindowBody() {
        return body;
    }
}

/**
 * Header segment for AddWindow
 */
class AddWindowHeader extends HBox {
    private Button returnButton;
    private ComboBox<String> mealTypeDropMenu;

    AddWindowHeader() {
        // Set header appearance
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(30);

        Text titleText = new Text("Add Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.setAlignment(Pos.CENTER); // Align the text to the Center

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        returnButton = new Button("Return");
        returnButton.setStyle(defaultButtonStyle);

        String meal_types[] = { "Breakfast", "Lunch", "Dinner" };
        mealTypeDropMenu = new ComboBox<>(FXCollections.observableArrayList(meal_types));
        mealTypeDropMenu.setStyle(defaultButtonStyle);
        this.getChildren().addAll(titleText, returnButton, mealTypeDropMenu);
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public String getMealType() {
        return mealTypeDropMenu.getValue();
    }
}

/**
 * Center segment for AddWindow
 */
class AddWindowBody extends VBox {
    private Label titleLabel;
    private TextField title;
    private Label descriptionLabel;
    private TextArea description;

    AddWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        titleLabel = new Label();
        titleLabel.setText("Title");
        titleLabel.setPrefSize(100, 30);

        title = new TextField();
        title.setPrefSize(400, 50);

        descriptionLabel = new Label();
        descriptionLabel.setText("Description");

        description = new TextArea();
        description.setPrefSize(200, 300);

        this.getChildren().addAll(titleLabel, title, descriptionLabel, description);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getDescription() {
        return description.getText();
    }

    public void clear() {
        title.clear();
        description.clear();
    }
}

/**
 * Footer segment for AddWindow
 */
class AddWindowFooter extends HBox {
    private Button completeButton;

    AddWindowFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        completeButton = new Button();
        completeButton.setText("Complete");

        this.getChildren().addAll(completeButton);
    }

    public Button getCompleteButton() {
        return completeButton;
    }
}

/**
 * Add window layout
 */
class AddWindow extends BorderPane {
    private AddWindowHeader header;
    private AddWindowBody body;
    private AddWindowFooter footer;

    AddWindow() {
        header = new AddWindowHeader();
        body = new AddWindowBody();
        footer = new AddWindowFooter();

        this.setTop(header);
        this.setCenter(body);
        this.setBottom(footer);
    }

    public AddWindowHeader getAddWindowHeader() {
        return header;
    }

    public AddWindowBody getAddWindowBody() {
        return body;
    }

    public AddWindowFooter getAddWindowFooter() {
        return footer;
    }
}

/**
 * Footer segment for RecipeViewWindow
 */
class RecipeViewFooter extends HBox {
    private Button saveButton;
    private Button returnButton;

    RecipeViewFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        saveButton = new Button();
        saveButton.setText("Save");

        returnButton = new Button();
        returnButton.setText("Return");

        this.getChildren().addAll(saveButton, returnButton);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

}

/**
 * Recipe details window layout
 */
class RecipeDetailsView extends BorderPane {
    private TextArea descriptionTextArea;
    private RecipeViewFooter footer;

    RecipeDetailsView(Recipe recipe, Scene previousScene) {
        footer = new RecipeViewFooter();
        this.setStyle("-fx-background-color: #F0F8FF;");

        String recipeTitle = recipe.getTitle();
        Text titleText = new Text(recipeTitle);
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        String recipeDescription = recipe.getDescription();
        descriptionTextArea = new TextArea(recipeDescription);
        descriptionTextArea.setPrefSize(400, 300);

        // Return button retrieves current stage and returns to the previous scene
        // (main)
        footer.getReturnButton().setOnAction(e -> {
            Stage stage = (Stage) footer.getReturnButton().getScene().getWindow();
            stage.setScene(previousScene);
        });

        this.setTop(titleText);
        this.setCenter(descriptionTextArea);
        this.setBottom(footer);

        footer.getSaveButton().setOnAction(e -> {
            recipe.setDescription(this.getDescription());
        });
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }
}

public class PantryPal extends Application {
    public static List<Recipe> recipeStorage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initiate recipe storage
        recipeStorage = new ArrayList<>();

        // Setting the layout of the MainWindow
        MainWindow mainWindow = new MainWindow();
        Scene mainScene = new Scene(mainWindow, 500, 400);

        // Setting the layout of the AddWindow
        AddWindow addWindow = new AddWindow();
        Scene addScene = new Scene(addWindow, 500, 400);

        // Setting the layout of the SuggestWindow
        SuggestWindow suggestWindow = new SuggestWindow();
        Scene suggestScene = new Scene(suggestWindow, 500, 400);

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(mainScene);

        // Link addRecipeButton with its function
        Button addRecipeButton = mainWindow.getHeader().getAddButton();
        addRecipeButton.setOnAction(e -> primaryStage.setScene(suggestScene));

        // Link returnButton with its function
        Button returnButton = addWindow.getAddWindowHeader().getReturnButton();
        AddWindowHeader addWindowHeader = addWindow.getAddWindowHeader();
        AddWindowBody addWindowBody = addWindow.getAddWindowBody();
        returnButton.setOnAction(e -> {
            // Return to main list and clear texts added
            addWindowBody.clear();
            primaryStage.setScene(mainScene);
        });

        // Link returnButton in SuggestWindow with its function
        Button returnSuggestButton = suggestWindow.getSuggestWindowHeader().getReturnButton();
        returnSuggestButton.setOnAction(e -> primaryStage.setScene(mainScene));

        // Link completeButton with its function
        Button completeButton = addWindow.getAddWindowFooter().getCompleteButton();
        completeButton.setOnAction(e -> {
            String title = addWindowBody.getTitle();
            String description = addWindowBody.getDescription();
            String mealType = addWindowHeader.getMealType();
            Recipe newRecipe = new Recipe(title, description, mealType);
            RecipeList recipeList = mainWindow.getRecipeList();

            // Store recipe in storage for view/delete/edit
            recipeStorage.add(newRecipe);
            recipeList.addRecipe(title);

            // Clear text in addWindow
            addWindowBody.clear();

            // Switch back to mainScene
            primaryStage.setScene(mainScene);
        });

        // Make window non-resizable
        primaryStage.setResizable(false);

        // Show the app
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}