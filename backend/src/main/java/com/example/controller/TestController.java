package com.example.controller;

import com.example.model.UserPreferences;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

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
    public UserPreferences getInfo(
            @RequestParam String mood,
            @RequestParam String genre,
            @RequestParam boolean workingDay){

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setMood(mood);
        userPreferences.setGenre(genre);
        userPreferences.setWorkingDay(workingDay);

        return userPreferences;
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
