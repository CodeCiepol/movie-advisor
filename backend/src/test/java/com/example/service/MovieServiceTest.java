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
        int new_mood = 4;
        String favouriteGenre = "Action";
        double favouriteGenreFactor = 3;
        boolean workingDay = true;
        MovieService movieService = new MovieService();
        Map<String, Double> decisionTree = new HashMap<>();
        decisionTree = movieService.decisionTreeGenreProbabilityV2(mood, favouriteGenre, workingDay);
        System.out.println("stary program:\n"+decisionTree);
        Map<String, Double> decisionMatrix = new HashMap<>();
        decisionMatrix = movieService.decisionTreeGenreProbabilityMatrix(new_mood,favouriteGenre, workingDay,favouriteGenreFactor);
        System.out.println("nowy program:\n"+decisionMatrix);
    }
    @Test
    void test_genre_stats(){
        MovieService movieService = new MovieService();
        Map<String, Map<String, Integer>> genres = new HashMap<>();

        Map<String, Integer> drama = new HashMap<>();
        drama.put("Joy", 33);
        drama.put("Sadness", 32);
        drama.put("Fear", 12);
        drama.put("Disgust", 10);
        drama.put("Anger", 13);
        genres.put("Drama", drama);

        Map<String, Integer> comedy = new HashMap<>();
        comedy.put("Joy", 40);
        comedy.put("Sadness", 27);
        comedy.put("Fear", 11);
        comedy.put("Disgust", 10);
        comedy.put("Anger", 12);
        genres.put("Comedy", comedy);

        Map<String, Integer> action = new HashMap<>();
        action.put("Joy", 25);
        action.put("Sadness", 28);
        action.put("Fear", 15);
        action.put("Disgust", 13);
        action.put("Anger", 19);
        genres.put("Action", action);
        System.out.println(Arrays.toString(movieService.getNormalizedGenreStatistics(genres, "Comedy")));
    }

}