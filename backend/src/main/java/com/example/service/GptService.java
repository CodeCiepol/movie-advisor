package com.example.service;

import com.example.model.UserPreferences;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;


public class GptService {
    private final String apiKey;
    private int moodMaxValue;

    public GptService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
        this.moodMaxValue = 4;
    }

    public String sendRequest(String userInput) throws IOException {
        String urlString = "https://api.openai.com/v1/chat/completions";
        String jsonInputString = new JSONObject()
                .put("model", "gpt-4o")
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
    public JSONObject getOneMovie(UserPreferences userPreferences, List<String> Movies) throws IOException {
        GptService openAIService = new GptService();
        try {
            String mood = String.valueOf(userPreferences.getMoodInt());
            String best_genre = userPreferences.getGenre();
            String message_isUserWorkedToday = userPreferences.isWorkingDay()? ". User worked today.":". User didn't work today.";
            String movies = " " + String.join(", ", Movies) + ". ";
            String userInput = "You are an advisor. Which movie should the user watch today? "+"Available movies:" +
                    movies + "User is feeling " + mood + "/4 (0-sad, 4-happy) and their best genre is " + best_genre +
                    message_isUserWorkedToday + " Answer in JSON format: {'name': name of movie, 'description': only 1 " +
                    "sentence why this movie in polish language, use second person }. DO NOT add any new line command,start with {.";
            System.out.println(userInput);
            String response = openAIService.sendRequest(userInput);
            System.out.println(response);
            JSONObject jsonResponse = new JSONObject(response);
            String contentString = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            JSONObject contentJson = new JSONObject(contentString);
            String nameOfMovie = contentJson.getString("name");
            String descriptionOfMovie = contentJson.getString("description");
//            String descriptionOfMovie = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getJSONObject("content").getString("description");
            String reply = nameOfMovie + ": " + descriptionOfMovie;
            return contentJson;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String test(UserPreferences userPreferences, List<String> Movies) throws IOException {
        GptService openAIService = new GptService();
            String mood = userPreferences.getMood();
            String best_genre = userPreferences.getGenre();
            String message_isUserWorkedToday = userPreferences.isWorkingDay()? ". User worked today.":". User didn't work today.";
            String movies = " " + String.join(", ", Movies) + ". ";
            String userInput = "You are an advisor. Which movie should the user watch today? "+"Available movies:" + movies + "User is " + mood + " and their best genre is " + best_genre + message_isUserWorkedToday + "Answer in JSON format: 'name': name of movie, 'description': only 1 sentence why this movie in polish language, use second person";
            return userInput;
    }
}
