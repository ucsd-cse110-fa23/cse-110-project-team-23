package project.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.*;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.control.TextField;

public class WhisperAPI {
    private String filePath;
    private TextField ingredientTextField;

    private final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private final String TOKEN = "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";
    private final String MODEL = "whisper-1";

    public WhisperAPI(String filePath, TextField ingredientTextField) {
        this.filePath = filePath;
        this.ingredientTextField = ingredientTextField;
    }

    public void transcribeAudio() {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Create file object from file path
                            File audioFile = new File(filePath);

                            // Set up HTTP connection
                            URL url = new URI(API_ENDPOINT).toURL();
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);

                            // Set up request headers
                            String boundary = "Boundary-" + System.currentTimeMillis();
                            connection.setRequestProperty(
                                    "Content-Type",
                                    "multipart/form-data; boundary=" + boundary);
                            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

                            // Set up output stream to write request body
                            OutputStream outputStream = connection.getOutputStream();

                            // Write model parameter to request body
                            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

                            // Write file parameter to request body
                            writeFileToOutputStream(outputStream, audioFile, boundary);

                            // Write closing boundary to request body
                            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

                            // Flush and close output stream
                            outputStream.flush();
                            outputStream.close();

                            // Get response code
                            int responseCode = connection.getResponseCode();

                            // Check response code and handle response accordingly
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                handleSuccessResponse(connection);
                            } else {
                                handleErrorResponse(connection);
                            }

                            // Disconnect connection
                            connection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        t.start();
    }

    // Helper method to write a parameter to the output stream in multipart form
    // data format
    private void writeParameterToOutputStream(
            OutputStream outputStream,
            String parameterName,
            String parameterValue,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data
    // format
    private void writeFileToOutputStream(
            OutputStream outputStream,
            File file,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                        file.getName() +
                        "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    private void handleSuccessResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());

        String transcribedText = responseJson.getString("text");

        // update text field
        ingredientTextField.setText(transcribedText);
    }

    private void handleErrorResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        // Handle error response as needed
    }
}
