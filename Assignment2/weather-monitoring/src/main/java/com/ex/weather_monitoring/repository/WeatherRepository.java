package com.ex.weather_monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ex.weather_monitoring.model.WeatherData;
import java.util.*;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData,Long>{
    List<WeatherData> findTop2ByOrderByTimestampDesc();

    @Query("SELECT w FROM WeatherData w WHERE DATE(w.timestamp) = :date")
    List<WeatherData> findWeatherByDate(Date date);

    // Calculate average temperature for a given day
    @Query("SELECT AVG(w.temperature) FROM WeatherData w WHERE DATE(w.timestamp) = :date")
    Double findAverageTemperatureByDate(Date date);

    // Calculate max temperature for a given day
    @Query("SELECT MAX(w.temperature) FROM WeatherData w WHERE DATE(w.timestamp) = :date")
    Double findMaxTemperatureByDate(Date date);

    // Calculate min temperature for a given day
    @Query("SELECT MIN(w.temperature) FROM WeatherData w WHERE DATE(w.timestamp) = :date")
    Double findMinTemperatureByDate(Date date);

    // Find dominant weather condition for a specific day (most frequent condition)
    @Query("SELECT w.mainWeather FROM WeatherData w WHERE DATE(w.timestamp) = :date GROUP BY w.mainWeather ORDER BY COUNT(w.mainWeather) DESC")
    String findDominantWeatherConditionByDate(Date date);
}
