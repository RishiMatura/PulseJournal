package com.journaling.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Code;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherResponse{

    private Current current;

@Getter
@Setter
    public class Current{

        private int temperature;

        @JsonProperty("weather_descriptions")
        private ArrayList<String> weatherDescriptions;

        private int feelslike;
    }

}






