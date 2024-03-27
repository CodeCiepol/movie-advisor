package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Movie {

    String title;
    float vote_average;
    String [] genres;

    public Movie(String title, int vote_average, String[] genres) {
        this.title = title;
        this.vote_average = vote_average;
        this.genres = genres;
    }
}
