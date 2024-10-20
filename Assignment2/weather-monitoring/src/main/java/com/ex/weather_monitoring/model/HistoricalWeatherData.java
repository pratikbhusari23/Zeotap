package com.ex.weather_monitoring.model;

import java.time.LocalDate;

public class HistoricalWeatherData {
    
    private LocalDate date; // Date of the record
    private double avgTemp; // Average temperature for that date

    public HistoricalWeatherData(LocalDate date, double avgTemp) {
        this.date = date;
        this.avgTemp = avgTemp;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

}
