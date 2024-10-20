package com.ex.weather_monitoring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.weather_monitoring.model.HistoricalWeatherData;
import com.ex.weather_monitoring.service.WeatherService;
import java.util.*;

@Controller
public class WeatherController {
    

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to fetch real-time weather data for Indian metros.
     */
    @GetMapping("/fetch-weather")
    @ResponseBody
    public Map<String,Object> fetchWeatherData() {
      
           weatherService.fetchWeatherData(); // Fetch weather data from the API
    // Assuming the service can return the processed summary data
    Map<String, Object> response = new HashMap<>();
    response.put("objective", "Develop a real-time data processing system to monitor weather conditions and provide summarized insights using rollups and aggregates.");
    response.put("dataSource", "The system continuously retrieves weather data from the OpenWeatherMap API.");
    response.put("weatherData", weatherService.getWeatherDataSummary()); // This should return a summary of the weather data fetched
    response.put("avgTemp", weatherService.getAvgTemperature());
    response.put("maxTemp", weatherService.getMaxTemperature());
    response.put("minTemp", weatherService.getMinTemperature());
    response.put("dominantWeather", weatherService.getDominantWeather());

    return response;
    }

    /**
     * Endpoint to perform daily summary rollup for a specific city.
     * 
     * @param city The city for which to perform the daily rollup.
     * @return Summary message
     */
    @GetMapping("/perform-daily-summary")
    public String performDailySummary(@RequestParam String city,Model model) {
      
        model.addAttribute("message", "Daily summary generated for " + city);
         // Pass the summary data
        return "index";
    }

    /**
     * Endpoint to check if any weather alert thresholds have been breached.
     * 
     * @param tempThreshold The temperature threshold to check.
     * @param conditionThreshold The weather condition to check.
     * @return Alert message
     */
    @GetMapping("/check-alerts")
    public String checkAlerts(@RequestParam double tempThreshold, @RequestParam String conditionThreshold , Model model) {
        weatherService.checkAlerts(tempThreshold, conditionThreshold);
        return "Alerts checked against thresholds";
    }
    @GetMapping("/fetch-historical-weather")
    @ResponseBody
    public List<HistoricalWeatherData> fetchHistoricalWeather() {
        return weatherService.getHistoricalWeatherData(); // Call the service method to fetch historical data
    }

}
