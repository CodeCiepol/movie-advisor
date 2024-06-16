package com.example.controller;

import com.example.model.UserPreferences;
import com.example.service.GptService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@CrossOrigin
public class TestController {

    @GetMapping("/test1")
    public String greet() {
        return "Witaj";
    }

    @PostMapping("/test2")
    public String testEndpoint(@RequestBody String requestBody) {
        return requestBody;
    }

    @GetMapping("/test3")
    public ExampleData getExampleData() {
        return new ExampleData("Jacob", 20, "jcb12@example.com");
    }

    @GetMapping("/choose-movie")
    public String getInfo(
            @RequestParam String mood,
            @RequestParam String genre,
            @RequestParam boolean workingDay){

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setMood(mood);
        userPreferences.setGenre(genre);
        userPreferences.setWorkingDay(workingDay);
        GptService openAIService = new GptService();
        try {
            JSONObject response = openAIService.getOneMovie(userPreferences, List.of("Inception", "The Matrix", "Interstellar"));
            return response.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject error = new JSONObject();
        error.put("name", "error");
        error.put("description", "error");
        return error.toString();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class ExampleData {
        private String name;
        private int age;
        private String email;
    }
}
