package com.example.service;

import com.example.model.Movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;


@Service
public class MovieService {

    public List<Movie> getMoviesFromCSV(String filePath) throws IOException, CsvException {
        List<Movie> movies = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            // Pomijamy nagłówek pliku CSV
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String title = line[0];
                float vote_average = Float.parseFloat(line[1]);
                String[] genres = line[2].split(";"); // Zakładając, że gatunki są oddzielone średnikiem
                movies.add(new Movie(title, (int) vote_average, genres));
            }
        }

        return movies;
    }

    // version optimized if-else statements
    public Map<String, Double> decisionTreeGenreProbabilityV2(String mood, String favouriteGenre, boolean workingDay){
        Map<String, Double> chosenGenres = new HashMap<>();
        chosenGenres.put("Drama", 0.34);
        chosenGenres.put("Comedy", 0.33);
        chosenGenres.put("Action", 0.33);

        double dramaChange = 0;
        double comedyChange = 0;
        double actionChange = 0;

        if (workingDay) {
            dramaChange -= 0.1;
            comedyChange += 0.05;
            actionChange += 0.05;
        } else {
            dramaChange += 0.1;
            comedyChange -= 0.05;
            actionChange -= 0.05;
        }

        if (mood.equals("Happy")) {
            dramaChange += 0.1;
            comedyChange -= 0.05;
            actionChange -= 0.05;
        } else if (mood.equals("Sad")) {
            dramaChange -= 0.15;
            comedyChange += 0.2;
            actionChange -= 0.05;
        }
        // if mood == "neutral" nothing changes

        if (favouriteGenre.equals("Drama")) {
            dramaChange += 0.2;
            comedyChange -= 0.1;
            actionChange -= 0.1;
        } else if (favouriteGenre.equals("Comedy")) {
            dramaChange -= 0.1;
            comedyChange += 0.2;
            actionChange -= 0.1;
        } else if (favouriteGenre.equals("Action")) {
            dramaChange -= 0.1;
            comedyChange -= 0.1;
            actionChange += 0.2;
        }

        double finalDramaChange = dramaChange;
        double finalComedyChange = comedyChange;
        double finalActionChange = actionChange;
        chosenGenres.compute("Drama", (key, value) -> value + finalDramaChange);
        chosenGenres.compute("Comedy", (key, value) -> value + finalComedyChange);
        chosenGenres.compute("Action", (key, value) -> value + finalActionChange);

        return chosenGenres;
    }

    //function to chose movies
    public List<String> chooseGenres(Map<String, Double> chosenGenres){

        List<String> genresList = new ArrayList<>();
        Random random = new Random();

        // Sum all probabilities
        double sum = chosenGenres.values().stream().mapToDouble(Double::doubleValue).sum();

        // Randomly choose 5 genres according to their probabilities
        for (int i = 0; i < 5; i++) {
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

        return genresList;
    }

    public List<String> chooseMovies(List<String> genresList) {
        List<String> chosenMovies = new ArrayList<>();
        List<String> allMovies = new ArrayList<>();

        // Wczytanie pliku CSV
        try (BufferedReader br = new BufferedReader(new FileReader("dataset/TMDB_movie_dataset_v2_filtered.csv"))) {
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
            double voteAverage = Double.parseDouble(parts[1]);
            String genres = parts[2].replaceAll("\"", "");

            // Sprawdź, czy film zawiera którykolwiek z gatunków z genresList i ma ocenę powyżej 7.0
            for (String genre : genresList) {

                if (genres.contains(genre) && voteAverage > 7.0 && !chosenMovies.contains(title)) {
                    chosenMovies.add(title);
                    break;
                }
            }

            // Jeśli znaleziono filmy dla wszystkich gatunków, przerwij pętlę
            if (chosenMovies.size() == genresList.size()) {
                break;
            }
        }

        return chosenMovies;
    }

}