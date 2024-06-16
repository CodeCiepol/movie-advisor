package com.example.controller;

import com.example.model.Movie;
import com.example.model.UserPreferences;
import com.example.service.MovieService;
import com.example.service.GptService;
import com.opencsv.exceptions.CsvException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/getMovies")
    public List<Movie> getMovies() {
        try {
            List<Movie> movies = movieService.getMoviesFromCSV("dataset/TMDB_movie_dataset_v2_filtered.csv");
            return movies;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null; // Możesz obsłużyć błędy w dowolny sposób, np. zwracając pusty listę
        }
    }

    @GetMapping("/movie")
    public Movie getMovieInfo(@RequestParam String mood, @RequestParam List<String> genres) {

        try {
            List<Movie> movies = movieService.getMoviesFromCSV("dataset/TMDB_movie_dataset_v2_filtered.csv");

            // Filtrowanie filmów względem gatunków
            Movie movie = movies.stream()
                    .filter(m -> containsAllGenres(m, genres))
                    .findFirst()
                    .orElse(null);

            return movie;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean containsAllGenres(Movie movie, List<String> genres) {
        List<String> movieGenres = List.of(movie.getGenres());
        for (String genre : genres) {
            if (!movieGenres.contains(genre)) {
                return false;
            }
        }
        return true;
    }


    @GetMapping("/findMovie")
    public String findBestMovie(
            @RequestParam int mood,
            @RequestParam String genre,
            @RequestParam boolean workingDay) {

        Map<String, Double> decisionTree = new HashMap<>();
        List<String> chooseGenres = new ArrayList<>();
        List<String> chooseMovies = new ArrayList<>();

//        decisionTree = movieService.decisionTreeGenreProbabilityV2(mood, genre, workingDay);
        decisionTree = movieService.decisionTreeGenreProbabilityMatrix(mood,genre,workingDay,1.4);
        chooseGenres = movieService.chooseGenres(decisionTree);
        chooseMovies = movieService.chooseMovies(chooseGenres);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setMoodInt(mood);
        userPreferences.setGenre(genre);
        userPreferences.setWorkingDay(workingDay);
        GptService openAIService = new GptService();
        try {
            JSONObject response = openAIService.getOneMovie(userPreferences, chooseMovies);
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject error = new JSONObject();
        error.put("name", "error");
        error.put("description", "error");
        return error.toString();
    }
}
