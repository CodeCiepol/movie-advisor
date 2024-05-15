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
    void chooseMovies (){

        List<String> genresList = Arrays.asList("Drama", "Comedy", "Action", "Drama", "Comedy");
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
}