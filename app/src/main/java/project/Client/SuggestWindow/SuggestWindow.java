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