package project.Client.AddWindow;

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
 * Footer segment for AddWindow
 */
public class AddWindowFooter extends HBox {
    private Button completeButton;
    private Button refreshButton;

    public AddWindowFooter() {
        this.setStyle("-fx-background-color: #F0F8FF;");

        completeButton = new Button();
        completeButton.setText("Complete");

        refreshButton = new Button();
        refreshButton.setText("Refresh");

        this.setSpacing(10);

        this.getChildren().addAll(completeButton,refreshButton);
    }

    public Button getCompleteButton() {
        return completeButton;
    }
        public Button getRefreshButton() {
        return refreshButton;
    }
}