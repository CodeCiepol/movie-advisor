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

    public double[] getNormalizedGenreStatistics(Map<String, Map<String, Integer>> genres, String genreName) {
        if (genres.containsKey(genreName)) {
            Map<String, Integer> genreStats = genres.get(genreName);
            double sum = genreStats.values().stream().mapToDouble(Integer::doubleValue).sum();
            double[] normalizedStats = new double[genreStats.size()];
            normalizedStats[0]=genreStats.get("Joy")/sum;
            normalizedStats[1]=genreStats.get("Sadness")/sum;
            normalizedStats[2]=genreStats.get("Fear")/sum;
            normalizedStats[3]=genreStats.get("Disgust")/sum;
            normalizedStats[4]=genreStats.get("Anger")/sum;
            System.out.println(Arrays.toString(normalizedStats));
            return normalizedStats;
        } else {
            System.out.println("Genre not found: " + genreName);
            return new double[0];
        }
    }

    public Map<String, Double> decisionTreeGenreProbabilityMatrix(int mood, String favouriteGenre, boolean workingDay, double favouriteGenreFactor, boolean isFocusOnFavouriteGenre) {

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

        // mood
        double[][] moodWeights = {
//         {Joy, Sadness, Fear, Disgust, Anger}
//              { J ,  S ,  F ,  D ,  A }
                {0.5, 0.2, 0.1, 0.0, 0.2},  // Very negative
                {0.3, 0.2, 0.2, 0.0, 0.3},
                {0.2, 0.2, 0.2, 0.2, 0.2}, //neutral
                {0.2, 0.3, 0.2, 0.1, 0.2},
                {0.1, 0.3, 0.2, 0.1, 0.3}   // Very positive
        };
        double[] weights = moodWeights[mood];

        // working day
        if(workingDay) {
           double[] differenceAfterWork =  {0.1,0.1,-0.1, -0.1,0.0};
            for (int i = 0; i < weights.length; i++) {
                weights[i] += differenceAfterWork[i];
                if (weights[i] <0){
                    weights[i] = 0;
                    weights[i-1] -= 0.1;
                }
            }
        }

        //best genre
        double[] bestGenreStats = getNormalizedGenreStatistics(genres,favouriteGenre);

        for (int i = 0; i<weights.length;i++){
            weights[i] += bestGenreStats[i] * favouriteGenreFactor;
        }
//        System.out.println("weights after best Genre:" + Arrays.toString(weights));

        // Calculate the score for each genre
        Map<String, Double> genreScores = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> genreEntry : genres.entrySet()) {
            String genre = genreEntry.getKey();
            Map<String, Integer> emotions = genreEntry.getValue();
            double score = 0.0;
            score += weights[0] * emotions.get("Joy");
            score += weights[1] * emotions.get("Sadness");
            score += weights[2] * emotions.get("Fear");
            score += weights[3] * emotions.get("Disgust");
            score += weights[4] * emotions.get("Anger");
            genreScores.put(genre, score);
        }

        double totalScore = genreScores.values().stream().mapToDouble(Double::doubleValue).sum();
        for (String genre : genreScores.keySet()) {
            double normalizedScored = (genreScores.get(genre) / totalScore) * 100;
            if (isFocusOnFavouriteGenre){
                normalizedScored -= 5;
                if (normalizedScored <0){
                    normalizedScored = 0;
                }
            }
            if (Objects.equals(genre, favouriteGenre)){
                normalizedScored +=5 * genres.size();
            }
            genreScores.put(genre,normalizedScored);
        }
        System.out.println("Genre probabilities:");
        System.out.println(genreScores);
        return genreScores;
    }

    public Map<String, Double> decisionTreeGenreProbabilityMatrixGpt(int mood, String favouriteGenre, boolean workingDay, double favouriteGenreFactor) {

        Map<String, Map<String, Integer>> genres = new HashMap<>();

        // Drama
        Map<String, Integer> drama = new HashMap<>();
        drama.put("Joy", 20);
        drama.put("Sadness", 40);
        drama.put("Fear", 10);
        drama.put("Disgust", 10);
        drama.put("Anger", 20);
        genres.put("Drama", drama);

// Comedy
        Map<String, Integer> comedy = new HashMap<>();
        comedy.put("Joy", 50);
        comedy.put("Sadness", 15);
        comedy.put("Fear", 10);
        comedy.put("Disgust", 10);
        comedy.put("Anger", 15);
        genres.put("Comedy", comedy);

// Action
        Map<String, Integer> action = new HashMap<>();
        action.put("Joy", 10);
        action.put("Sadness", 20);
        action.put("Fear", 35);
        action.put("Disgust", 10);
        action.put("Anger", 25);
        genres.put("Action", action);

// Science Fiction
        Map<String, Integer> scienceFiction = new HashMap<>();
        scienceFiction.put("Joy", 10);
        scienceFiction.put("Sadness", 15);
        scienceFiction.put("Fear", 20);
        scienceFiction.put("Disgust", 10);
        scienceFiction.put("Anger", 45);
        genres.put("Science Fiction", scienceFiction);

// Adventure
        Map<String, Integer> adventure = new HashMap<>();
        adventure.put("Joy", 25);
        adventure.put("Sadness", 15);
        adventure.put("Fear", 10);
        adventure.put("Disgust", 15);
        adventure.put("Anger", 35);
        genres.put("Adventure", adventure);

// Crime
        Map<String, Integer> crime = new HashMap<>();
        crime.put("Joy", 10);
        crime.put("Sadness", 25);
        crime.put("Fear", 20);
        crime.put("Disgust", 35);
        crime.put("Anger", 10);
        genres.put("Crime", crime);

// Thriller
        Map<String, Integer> thriller = new HashMap<>();
        thriller.put("Joy", 10);
        thriller.put("Sadness", 35);
        thriller.put("Fear", 25);
        thriller.put("Disgust", 20);
        thriller.put("Anger", 10);
        genres.put("Thriller", thriller);

// Fantasy
        Map<String, Integer> fantasy = new HashMap<>();
        fantasy.put("Joy", 30);
        fantasy.put("Sadness", 20);
        fantasy.put("Fear", 25);
        fantasy.put("Disgust", 10);
        fantasy.put("Anger", 40);
        genres.put("Fantasy", fantasy);

// Horror
        Map<String, Integer> horror = new HashMap<>();
        horror.put("Joy", 10);
        horror.put("Sadness", 20);
        horror.put("Fear", 40);
        horror.put("Disgust", 10);
        horror.put("Anger", 10);
        genres.put("Horror", horror);

// Animation
        Map<String, Integer> animation = new HashMap<>();
        animation.put("Joy", 45);
        animation.put("Sadness", 10);
        animation.put("Fear", 10);
        animation.put("Disgust", 5);
        animation.put("Anger", 30);
        genres.put("Animation", animation);

        // mood
        double[][] moodWeights = {
//         {Joy, Sadness, Fear, Disgust, Anger}
//              { J ,  S ,  F ,  D ,  A }
                {0.5, 0.2, 0.1, 0.0, 0.2},  // Very negative
                {0.3, 0.2, 0.2, 0.0, 0.3},
                {0.2, 0.2, 0.2, 0.1, 0.3}, //neutral
                {0.2, 0.3, 0.2, 0.1, 0.2},
                {0.1, 0.3, 0.2, 0.1, 0.3}   // Very positive
        };
        double[] weights = moodWeights[mood];

        // working day
        if(workingDay) {
            double[] differenceAfterWork =  {0.1,0.1,-0.1, -0.1,0.0};
            for (int i = 0; i < weights.length; i++) {
                weights[i] += differenceAfterWork[i];
                if (weights[i] <0){
                    weights[i] = 0;
                    weights[i-1] -= 0.1;
                }
            }
        }

        //best genre
        double[] bestGenreStats = getNormalizedGenreStatistics(genres,favouriteGenre);

        for (int i = 0; i<weights.length;i++){
            weights[i] += bestGenreStats[i] * favouriteGenreFactor;
        }

        // Calculate the score for each genre
        Map<String, Double> genreScores = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> genreEntry : genres.entrySet()) {
            String genre = genreEntry.getKey();
            Map<String, Integer> emotions = genreEntry.getValue();
            double score = 0.0;
            score += weights[0] * emotions.get("Joy");
            score += weights[1] * emotions.get("Sadness");
            score += weights[2] * emotions.get("Fear");
            score += weights[3] * emotions.get("Disgust");
            score += weights[4] * emotions.get("Anger");
            genreScores.put(genre, score);
        }

        double totalScore = genreScores.values().stream().mapToDouble(Double::doubleValue).sum();
        for (String genre : genreScores.keySet()) {
            genreScores.put(genre, (genreScores.get(genre) / totalScore) * 100);
        }

        return genreScores;
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