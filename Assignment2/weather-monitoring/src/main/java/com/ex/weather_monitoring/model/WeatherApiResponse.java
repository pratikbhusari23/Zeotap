package com.ex.weather_monitoring.model;

import java.util.*;

public class WeatherApiResponse {
    
    private List<Weather> weather;
    private Main main;

    // Getters and setters

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    // Inner classes to represent parts of the response

    public static class Weather {
        private String main;
        private String description;

        // Getters and setters

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Main {
        private double temp;
        private double feels_like;

        // Getters and setters

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeelsLike() {
            return feels_like;
        }

        public void setFeelsLike(double feelsLike) {
            this.feels_like = feelsLike;
        }
    }
}
