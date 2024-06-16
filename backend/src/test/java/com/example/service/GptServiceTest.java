package com.example.service;

import com.example.model.UserPreferences;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import java.io.IOException;

@SpringBootTest
class GptServiceTest {

	@Test
	void contextLoads() {
	}
	@Test
	void testGptPrompt(){
		GptService openAIService = new GptService();
		try {
			String response = openAIService.sendRequest("Rozumiesz język polski?");
			JSONObject jsonResponse = new JSONObject(response);
			String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testGetOneMovie(){
		GptService openAIService = new GptService();
		UserPreferences userPreferences = new UserPreferences("sad",0,"action",true);
		try {
			JSONObject response = openAIService.getOneMovie(userPreferences,List.of("Inception", "The Matrix", "Interstellar"));
			System.out.println(response);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testUserInput(){
		GptService openAIService = new GptService();
		UserPreferences userPreferences = new UserPreferences("sad",0,"action",true);
		try {
			String response = openAIService.test(userPreferences,List.of("Inception", "The Matrix", "Interstellar"));
			System.out.println(response);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
