package com.agustincoding.ragebaitgen.gemini;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeminiService {
    // Name of the configuration file containing URL and API key
    private static final String CONFIG_FILE = "config.properties";

    // Endpoint URL and API key loaded from the config.properties file
    private static final String URL_ENDPOINT;
    private static final String API_KEY;

    static {
        Properties prop = new Properties();
        try (InputStream is = GeminiService.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            // Reads the config.properties file from the classpath
            if (is == null) {
                throw new FileNotFoundException("config.properties not found in classpath");
            }
            prop.load(is);
            // Retrieves the URL and API key to use the external service
            URL_ENDPOINT = prop.getProperty("GEMINI_URL", "");
            API_KEY      = prop.getProperty("GEMINI_API_KEY", "");
        } catch (IOException e) {
            // If there’s an error loading the configuration, throw an exception that stops the app
            throw new RuntimeException("Error loading configuration: ", e);
        }
    }

    private GeminiService() {
        // Private constructor to prevent instantiation (utility class with only static methods)
    }

    /**
     * Main method that sends a message (prompt) to the Gemini service and returns the response as text.
     * This method performs:
     *  - Building the JSON with the message
     *  - HTTP POST connection with headers and body
     *  - Reading the JSON response
     *  - Parsing to extract only the relevant text and returning it
     */
    public static String getResponseTo(String message) {
        try {
            // Build the URL with the endpoint and API key as parameter
            URL url = new URL(URL_ENDPOINT + "?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST"); // POST method to send data
            conn.setRequestProperty("Content-Type", "application/json"); // JSON content type
            conn.setDoOutput(true); // Indicate that we’re sending data in the request body

            // Build the JSON with the user’s message
            String promptJson = buildPromptJson(message);

            // Send the JSON in the request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = promptJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response received from the server
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Convert the JSON response into plain text to use in the application
            return jsonToMessage(response.toString());

        } catch (IOException e) {
            // In case of error, print for debugging and return a message with the error
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Method that builds the JSON with the structure expected by the service, including the input text
    private static String buildPromptJson(String input) {
        return "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"text\": \"" + input.replace("\"", "\\\"") + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    /**
     * Method that receives the JSON response from the service and extracts the
     * relevant text from the first response candidate.
     * Returns the text to display in the application.
     */
    private static String jsonToMessage(String json) {
        try {
            JSONObject root       = new JSONObject(json);
            JSONArray candidates  = root.getJSONArray("candidates");
            if (candidates.isEmpty()) return "No candidates in response";

            // Take the first candidate and get its content and parts (text)
            JSONObject firstCand   = candidates.getJSONObject(0);
            JSONObject contentObj  = firstCand.getJSONObject("content");
            JSONArray parts        = contentObj.getJSONArray("parts");

            StringBuilder sb = new StringBuilder();
            // Concatenate all the text parts to build the complete response
            for (int i = 0; i < parts.length(); i++) {
                sb.append(parts.getJSONObject(i).getString("text"));
            }
            return sb.toString();

        } catch (Exception e) {
            // If there’s an error parsing the JSON, return an error message with details
            e.printStackTrace();
            return "Error parsing JSON: " + e.getMessage();
        }
    }
}
