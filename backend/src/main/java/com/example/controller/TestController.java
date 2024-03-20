package com.example.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test1")
    public String greet() {
        return "Witaj";
    }

    @PostMapping("/test2")
    public String testEndpoint(@RequestBody String requestBody) {
        return requestBody;
    }

    @GetMapping("/test3ggg")
    public ExampleData getExampleData() {
        return new ExampleData("Jacob", 20, "jcb12@example.com");
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
