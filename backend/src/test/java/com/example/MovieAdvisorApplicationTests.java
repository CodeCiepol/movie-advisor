package com.example;

import com.example.service.gpt_prompt;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import java.io.IOException;

@SpringBootTest
class MovieAdvisorApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void testGptPrompt(){
		gpt_prompt openAIService = new gpt_prompt();
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
		gpt_prompt openAIService = new gpt_prompt();
		try {
			String response = openAIService.getOneMovie(List.of("Inception", "The Matrix", "Interstellar"));
			System.out.println(response);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
