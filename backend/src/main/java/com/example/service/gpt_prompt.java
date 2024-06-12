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


public class gpt_prompt {
    private final String apiKey;

    public gpt_prompt() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
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
        gpt_prompt openAIService = new gpt_prompt();
        try {
            String mood = userPreferences.getMood();
            String best_genre = userPreferences.getGenre();
            String message_isUserWorkedToday = userPreferences.isWorkingDay()? ". User worked today.":". User didn't work today.";
            String movies = " " + String.join(", ", Movies) + ". ";
            String userInput = "You are an advisor. Which movie should the user watch today? "+"Available movies:" + movies + "User is " + mood + " and their best genre is " + best_genre + message_isUserWorkedToday + " Answer in JSON format: {'name': name of movie, 'description': only 1 sentence why this movie, use second person}. DO NOT add any new line command,start with {.";
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
        gpt_prompt openAIService = new gpt_prompt();
            String mood = userPreferences.getMood();
            String best_genre = userPreferences.getGenre();
            String message_isUserWorkedToday = userPreferences.isWorkingDay()? ". User worked today.":". User didn't work today.";
            String movies = " " + String.join(", ", Movies) + ". ";
            String userInput = "You are an advisor. Which movie should the user watch today? "+"Available movies:" + movies + "User is " + mood + " and their best genre is " + best_genre + message_isUserWorkedToday + "Answer in JSON format: 'name': name of movie, 'description': only 1 sentence why this movie, use second person";
            return userInput;
    }
    public Map<String, Double> decisionTreeGenreProbabilityV2(String mood, String favouriteGenre, boolean workingDay){
        Map<String, Double> chosenGenres = new HashMap<>();
        chosenGenres.put("Drama", 0.34);
        chosenGenres.put("Comedy", 0.33);
        chosenGenres.put("Action", 0.33);

        double dramaChange = 0;
        double comedyChange = 0;
        double actionChange = 0;

        if (workingDay) {
            dramaChange -= 0.1;
            comedyChange += 0.05;
            actionChange += 0.05;
        } else {
            dramaChange += 0.1;
            comedyChange -= 0.05;
            actionChange -= 0.05;
        }

        if (mood.equals("Happy")) {
            dramaChange += 0.1;
            comedyChange -= 0.05;
            actionChange -= 0.05;
        } else if (mood.equals("Sad")) {
            dramaChange -= 0.15;
            comedyChange += 0.2;
            actionChange -= 0.05;
        }
        // if mood == "neutral" nothing changes

        if (favouriteGenre.equals("Drama")) {
            dramaChange += 0.2;
            comedyChange -= 0.1;
            actionChange -= 0.1;
        } else if (favouriteGenre.equals("Comedy")) {
            dramaChange -= 0.1;
            comedyChange += 0.2;
            actionChange -= 0.1;
        } else if (favouriteGenre.equals("Action")) {
            dramaChange -= 0.1;
            comedyChange -= 0.1;
            actionChange += 0.2;
        }

        double finalDramaChange = dramaChange;
        double finalComedyChange = comedyChange;
        double finalActionChange = actionChange;
        chosenGenres.compute("Drama", (key, value) -> value + finalDramaChange);
        chosenGenres.compute("Comedy", (key, value) -> value + finalComedyChange);
        chosenGenres.compute("Action", (key, value) -> value + finalActionChange);

        return chosenGenres;
    }

    //function to chose movies
    public List<String> chooseGenres(Map<String, Double> chosenGenres){

        List<String> genresList = new ArrayList<>();
        Random random = new Random();

        // Sum all probabilities
        double sum = chosenGenres.values().stream().mapToDouble(Double::doubleValue).sum();

        // Randomly choose 5 genres according to their probabilities
        for (int i = 0; i < 5; i++) {
            double rand = random.nextDouble() * sum;
            double cumulativeProb = 0;
            for (Map.Entry<String, Double> entry : chosenGenres.entrySet()) {
                cumulativeProb += entry.getValue();
                if (rand <= cumulativeProb) {
                    genresList.add(entry.getKey());
                    break;
                }
            }
        }

        return genresList;
    }
}
