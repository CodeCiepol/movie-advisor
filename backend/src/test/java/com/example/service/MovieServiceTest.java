package com.example.service;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    @Test
    void decisionTreeGenreProbability() {
        Map<String, Double> chosenGenres = new HashMap<>();
        chosenGenres.put("Drama", 0.34);
        chosenGenres.put("Comedy", 0.33);
        chosenGenres.put("Action", 0.33);

        chosenGenres.compute("Drama", (key, value) -> value + 0.2);

        // Sprawdzenie, czy wartość dla klucza "Drama" jest równa 0.54
        assertEquals(0.54, chosenGenres.get("Drama"));
    }

    @Test
    void chooseGenres(){
        Map<String, Double> chosenGenres = new HashMap<>();
        chosenGenres.put("Drama", 0.34);
        chosenGenres.put("Comedy", 0.33);
        chosenGenres.put("Action", 0.33);

        List<String> genresList = new ArrayList<>();
        Random random = new Random();

        // Sum all probabilities
        double sum = chosenGenres.values().stream().mapToDouble(Double::doubleValue).sum();

        // Randomly choose 5 genres according to their probabilities
        for (int i = 0; i < 20; i++) {
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
        assertEquals(1, sum);

        System.out.println(genresList);
    }

    @Test
    void chooseMovies() {
        // Przygotowanie danych wejściowych
        List<String> genresList = Arrays.asList("Drama", "Comedy", "Action", "Drama", "Comedy", "Drama", "Drama", "Drama");
        List<String> chosenMovies = new ArrayList<>();

        List<String> allMovies = new ArrayList<>();

        // Wczytanie pliku CSV
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dawid\\Desktop\\ISA\\semestr 3\\movie-recomendation-system\\movie-advisor\\dataset\\TMDB_movie_dataset_v2_filtered.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                allMovies.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Losowo przemieszaj listę filmów
        Collections.shuffle(allMovies);

        // Iteruj przez przemieszaną listę filmów
        for (String movie : allMovies) {
            String[] parts = movie.split(",");
            String title = parts[0];
            String genres = parts[2].replaceAll("\"", "");

            // Sprawdź, czy film zawiera którykolwiek z gatunków z genresList
            for (String genre : genresList) {
                if (genres.contains(genre) && !chosenMovies.contains(title)) {
                    chosenMovies.add(title);
                    break;
                }
            }

            // Jeśli znaleziono filmy dla wszystkich gatunków, przerwij pętlę
            if (chosenMovies.size() == genresList.size()) {
                break;
            }
        }

        // Weryfikacja wyników
        assertEquals(genresList.size(), chosenMovies.size());
        System.out.println("Genres list: " + genresList);
        System.out.println("Chosen movies: " + chosenMovies);
    }


    @Test
    void choseMoviesv2(){

        List<String> genresList = Arrays.asList("Drama", "Comedy", "Action", "Drama", "Comedy", "Drama", "Drama", "Drama");
        Set<String> chosenGenres = new HashSet<>();
        Map<String, String> chosenMovies = new HashMap<>();
        List<String> allMovies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dawid\\Desktop\\ISA\\semestr 3\\movie-recomendation-system\\movie-advisor\\dataset\\TMDB_movie_dataset_v2_filtered.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                allMovies.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Losowo przemieszaj listę filmów
        Collections.shuffle(allMovies);
        // Iteruj przez przemieszaną listę filmów
        for (String movie : allMovies) {
            String[] parts = movie.split(",");
            String title = parts[0];
            String genres = parts[2].replaceAll("\"", "");
            // Sprawdź, czy film zawiera którykolwiek z gatunków z genresList
            for (String genre : genresList) {
                if (genres.contains(genre) && !chosenGenres.contains(genre)) {
                    chosenMovies.put(genre, title);
                    chosenGenres.add(genre);
                    break;
                }
            }
            // Jeśli znaleziono film dla każdego gatunku, przerwij pętlę
            if (chosenGenres.size() == genresList.size()) {
                break;
            }
        }
        System.out.println(genresList);
        System.out.println(chosenMovies);

    }
    // testing with weights(function with matrix
    @Test
    void genre_generation(){
        String mood = "Happy";
        int new_mood = 2;
        String favouriteGenre = "Drama";
        double favouriteGenreFactor = 5;
        boolean workingDay = false;
        MovieService movieService = new MovieService();

        Map<String, Double> decisionTree = new HashMap<>();
        decisionTree = movieService.decisionTreeGenreProbabilityV2(mood, favouriteGenre, workingDay);
        System.out.println("stary program:\n"+decisionTree);

        Map<String, Double> decisionMatrix = new HashMap<>();
        decisionMatrix = movieService.decisionTreeGenreProbabilityMatrix(new_mood,favouriteGenre, workingDay,favouriteGenreFactor,true);
        System.out.println("nowy program:\n"+decisionMatrix);

        Map<String, Double> decisionMatrixGpt = new HashMap<>();
        decisionMatrixGpt = movieService.decisionTreeGenreProbabilityMatrixGpt(new_mood,favouriteGenre, workingDay,favouriteGenreFactor);
        System.out.println("nowy program gpt:\n"+decisionMatrixGpt);
    }
    @Test
    void test_genre_stats(){
        MovieService movieService = new MovieService();
        Map<String, Map<String, Integer>> genres = new HashMap<>();

        // Drama
        Map<String, Integer> drama = new HashMap<>();
        drama.put("Joy", 33);
        drama.put("Sadness", 32);
        drama.put("Fear", 12);
        drama.put("Disgust", 10);
        drama.put("Anger", 13);
        genres.put("Drama", drama);

        // Comedy
        Map<String, Integer> comedy = new HashMap<>();
        comedy.put("Joy", 40);
        comedy.put("Sadness", 27);
        comedy.put("Fear", 11);
        comedy.put("Disgust", 10);
        comedy.put("Anger", 12);
        genres.put("Comedy", comedy);

        // Action
        Map<String, Integer> action = new HashMap<>();
        action.put("Joy", 25);
        action.put("Sadness", 28);
        action.put("Fear", 15);
        action.put("Disgust", 13);
        action.put("Anger", 19);
        genres.put("Action", action);

        // Science Fiction
        Map<String, Integer> scienceFiction = new HashMap<>();
        scienceFiction.put("Joy", 26);
        scienceFiction.put("Sadness", 28);
        scienceFiction.put("Fear", 19);
        scienceFiction.put("Disgust", 11);
        scienceFiction.put("Anger", 16);
        genres.put("Science Fiction", scienceFiction);

        // Adventure
        Map<String, Integer> adventure = new HashMap<>();
        adventure.put("Joy", 35);
        adventure.put("Sadness", 26);
        adventure.put("Fear", 14);
        adventure.put("Disgust", 10);
        adventure.put("Anger", 15);
        genres.put("Adventure", adventure);

        // Crime
        Map<String, Integer> crime = new HashMap<>();
        crime.put("Joy", 20);
        crime.put("Sadness", 26);
        crime.put("Fear", 14);
        crime.put("Disgust", 10);
        crime.put("Anger", 15);
        genres.put("Crime", crime);

        // Thriller
        Map<String, Integer> thriller = new HashMap<>();
        thriller.put("Joy", 19);
        thriller.put("Sadness", 30);
        thriller.put("Fear", 15);
        thriller.put("Disgust", 16);
        thriller.put("Anger", 19);
        genres.put("Thriller", thriller);

        // Fantasy
        Map<String, Integer> fantasy = new HashMap<>();
        fantasy.put("Joy", 35);
        fantasy.put("Sadness", 28);
        fantasy.put("Fear", 14);
        fantasy.put("Disgust", 9);
        fantasy.put("Anger", 14);
        genres.put("Fantasy", fantasy);

        // Horror
        Map<String, Integer> horror = new HashMap<>();
        horror.put("Joy", 20);
        horror.put("Sadness", 30);
        horror.put("Fear", 23);
        horror.put("Disgust", 12);
        horror.put("Anger", 15);
        genres.put("Horror", horror);

        // Animation
        Map<String, Integer> animation = new HashMap<>();
        animation.put("Joy", 50);
        animation.put("Sadness", 20);
        animation.put("Fear", 12);
        animation.put("Disgust", 7);
        animation.put("Anger", 11);
        genres.put("Animation", animation);
        System.out.println(genres.size());
        System.out.println(Arrays.toString(movieService.getNormalizedGenreStatistics(genres, "Comedy")));
    }

}