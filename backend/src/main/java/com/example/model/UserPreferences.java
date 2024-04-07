package com.example.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {

        private String mood;
        private String genre;
        private boolean workingDay;

}
