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

import java.util.*;

/**
 * Header segment for SuggestWindow
 */
public class SuggestWindow extends BorderPane {
    private SuggestWindowHeader header;
    private SuggestWindowBody body;

    public SuggestWindow() {
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

class SuggestWindowHeader extends HBox {
    private Button returnButton;

    public SuggestWindowHeader() {
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

    private static final String AUDIO_FILE_PATH = "recording.wav";

    public SuggestWindowBody() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        ingredientLabel = new Label();
        ingredientLabel.setText("Record your meal type and ingredients");
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

/**
 * Suggest window layout
 */

