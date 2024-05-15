package com.example.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;

public class gpt_prompt {
    private final String apiKey;

    public gpt_prompt() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
    }
    public String sendRequest(String userInput) throws IOException {
        String urlString = "https://api.openai.com/v1/chat/completions";
        String jsonInputString = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", new JSONObject[]{new JSONObject().put("role", "user").put("content", userInput)})
                .toString();

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(con.getInputStream(), StandardCharsets.UTF_8.name());
            String responseBody = scanner.useDelimiter("\\A").next();
            scanner.close();
            return responseBody;
        } else {
            throw new IOException("POST request failed with response code: " + responseCode);
        }
    }
    public String getOneMovie(List<String> Movies) throws IOException {
        gpt_prompt openAIService = new gpt_prompt();
        try {
            String emotion = "sad";
            String best_genre = "action";
            String movies = String.join(", ", Movies);
            String userInput = "User is " + emotion + " and their best genre is " + best_genre  + ". Which movie will be the best today? Available movies:"+movies+"Answer in JSON format: 'name': name of movie, 'description': only 1 sentence why this movie, use second person";
            String response = openAIService.sendRequest(userInput);
            JSONObject jsonResponse = new JSONObject(response);
            String contentString = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            JSONObject contentJson = new JSONObject(contentString);
            String nameOfMovie = contentJson.getString("name");
            String descriptionOfMovie = contentJson.getString("description");
//            String descriptionOfMovie = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getJSONObject("content").getString("description");
            String reply = nameOfMovie + ": " + descriptionOfMovie;
            return reply;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
