package project.Client.SuggestWindow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import project.Server.WhisperAPI;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;

import java.util.*;

/**
 * Center segment for SuggestWindow
 */
public class SuggestWindowBody extends VBox {
    private Label mealTypeLabel;
    private ComboBox<String> mealTypeBox;
    private Label ingredientLabel;
    private TextField ingredientTextField;
    private Button recordIngredientsButton;
    private Button confirmButton;
    private Label recordingLabel;
    private Label emptyLabel;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private boolean recording = false;

    private static final String AUDIO_FILE_PATH = "recording.wav";

    public SuggestWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        mealTypeLabel = new Label();
        mealTypeLabel.setText("Select your meal type");
        mealTypeBox = new ComboBox<>();
        mealTypeBox.getItems().addAll("Breakfast", "Lunch", "Dinner");
        mealTypeBox.setValue("Breakfast");

        ingredientLabel = new Label();
        ingredientLabel.setText("Record your ingredients");
        ingredientLabel.setPrefSize(400, 30);

        ingredientTextField = new TextField();
        ingredientTextField.setPrefSize(400, 50);

        recordIngredientsButton = new Button("Record");
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
                WhisperAPI whisper = new WhisperAPI(AUDIO_FILE_PATH, ingredientTextField);
                whisper.transcribeAudio();
            }
        });

        confirmButton.setStyle(defaultButtonStyle);

        // Add buttons to the layout
        HBox buttonBox = new HBox(recordIngredientsButton, confirmButton);
        buttonBox.setSpacing(10);

        audioFormat = getAudioFormat();

        this.getChildren().addAll(mealTypeLabel, mealTypeBox, ingredientLabel, ingredientTextField, buttonBox,
                recordingLabel);
    }

    public String getMealType() {
        return mealTypeBox.getValue();
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
        int channels = 1;

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

    public Button getConfirmButton() {
        return confirmButton;
    }

}