package com.example.controller;

import com.example.model.Movie;
import com.example.service.MovieService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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
            return null; // Możesz obsłużyć błędy w dowolny sposób, np. zwracając pusty listę
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
    public Movie findBestMovie(
            @RequestParam String frameOfMind,
            @RequestParam String genre,
            @RequestParam boolean workingDay){
        Movie movie = new Movie();

        //logika wybierająca wybór filmu


        return movie;
    }


}
