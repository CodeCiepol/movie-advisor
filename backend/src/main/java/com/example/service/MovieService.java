package com.example.service;

import com.example.model.Movie;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


}
