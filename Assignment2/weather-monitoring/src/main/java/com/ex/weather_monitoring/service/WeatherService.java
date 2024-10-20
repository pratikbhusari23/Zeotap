package com.ex.weather_monitoring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ex.weather_monitoring.model.DailySummary;
import com.ex.weather_monitoring.model.HistoricalWeatherData;
import com.ex.weather_monitoring.model.WeatherApiResponse;
import com.ex.weather_monitoring.model.WeatherData;
import com.ex.weather_monitoring.repository.DailySummaryRepository;
import com.ex.weather_monitoring.repository.WeatherRepository;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class WeatherService {
    
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private DailySummaryRepository dailySummaryRepository;

    private final String apiKey = "37ad5f1dfc8356eb53d13e376881e997";
    private final String[] metros = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};
    private Map<String, List<WeatherData>> dailyDataMap = new HashMap<>();

    /**
     * Fetch weather data from OpenWeatherMap API for predefined Indian metros.
     */
    public void fetchWeatherData() {
        RestTemplate restTemplate = new RestTemplate();
        for (String city : metros) {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (response != null && response.getWeather() != null && !response.getWeather().isEmpty()) {
                WeatherData weatherData = new WeatherData();
                weatherData.setCity(city);
                weatherData.setMainWeather(response.getWeather().get(0).getMain());
                weatherData.setTemperature(response.getMain().getTemp() - 273.15); // Convert Kelvin to Celsius
                weatherData.setFeelsLike(response.getMain().getFeelsLike() - 273.15);
                weatherData.setTimestamp(LocalDateTime.now());

                // Save data to database
                weatherRepository.save(weatherData);

                // Update daily map for rollups
                updateDailyDataMap(city, weatherData);
            }
        }
    }

    /**
     * Update the daily data map with the latest weather data.
     */
    private void updateDailyDataMap(String city, WeatherData weatherData) {
        LocalDate today = LocalDate.now();

        // Initialize the daily data list if not already present
        dailyDataMap.computeIfAbsent(city, k -> new ArrayList<>());

        // Add the latest weather data for the current day
        dailyDataMap.get(city).add(weatherData);

        // Perform rollups and aggregates at the end of the day
        if (weatherData.getTimestamp().toLocalDate().isAfter(today)) {
            performDailySummary(city);
        }
    }

    /**
     * Perform daily summary rollup and save to the database.
     */
    public void performDailySummary(String city) {
        List<WeatherData> dailyData = dailyDataMap.get(city);

        if (dailyData != null && !dailyData.isEmpty()) {
            double sumTemp = 0;
            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;
            Map<String, Integer> weatherConditionCount = new HashMap<>();

            for (WeatherData data : dailyData) {
                sumTemp += data.getTemperature();
                maxTemp = Math.max(maxTemp, data.getTemperature());
                minTemp = Math.min(minTemp, data.getTemperature());

                weatherConditionCount.put(data.getMainWeather(),
                        weatherConditionCount.getOrDefault(data.getMainWeather(), 0) + 1);
            }

            double avgTemp = sumTemp / dailyData.size();
            String dominantWeather = Collections.max(weatherConditionCount.entrySet(), Map.Entry.comparingByValue()).getKey();

            // Create and save the daily summary
            DailySummary summary = new DailySummary();
            summary.setCity(city);
            summary.setDate(LocalDate.now());
            summary.setAvgTemp(avgTemp);
            summary.setMaxTemp(maxTemp);
            summary.setMinTemp(minTemp);
            summary.setDominantWeather(dominantWeather);

            dailySummaryRepository.save(summary);

            // Clear the data for the next day
            dailyDataMap.get(city).clear();
        }
    }

    /**
     * Trigger alerts based on user-defined thresholds.
     */
    public void checkAlerts(double tempThreshold, String conditionThreshold) {
        List<WeatherData> recentData = weatherRepository.findTop2ByOrderByTimestampDesc();

        if (recentData.size() >= 2) {
            WeatherData latestData = recentData.get(0);
            WeatherData previousData = recentData.get(1);

            // Check for temperature threshold breach
            if (latestData.getTemperature() > tempThreshold && previousData.getTemperature() > tempThreshold) {
                triggerAlert("Temperature threshold exceeded: " + latestData.getTemperature() + "°C in " + latestData.getCity());
            }

            // Check for weather condition threshold breach
            if (latestData.getMainWeather().equals(conditionThreshold) && previousData.getMainWeather().equals(conditionThreshold)) {
                triggerAlert("Weather condition threshold exceeded: " + conditionThreshold + " in " + latestData.getCity());
            }
        }
    }

    /**
     * Trigger an alert (can be console log, email, etc.)
     */
    private void triggerAlert(String alertMessage) {
        System.out.println("ALERT: " + alertMessage);
        // You can extend this to send email or other notifications
    }

    public String getWeatherDataSummary() {
    // Logic to process the weather data and summarize it
    return "Weather data fetched for metros: " + String.join(", ", metros);
    }

      // Method to calculate the average temperature from today's data
      public double calculateAverageTemp() {
        List<WeatherData> weatherDataList = getTodayWeatherData();
        double sumTemp = 0;

        if (weatherDataList.isEmpty()) {
            return 0; // Return 0 if no data is available for today
        }

        for (WeatherData data : weatherDataList) {
            sumTemp += data.getTemperature();
        }

        return sumTemp / weatherDataList.size(); // Average
    }

    // Method to calculate the maximum temperature from today's data
    public double calculateMaxTemp() {
        List<WeatherData> weatherDataList = getTodayWeatherData();

        if (weatherDataList.isEmpty()) {
            return Double.MIN_VALUE; // Return a default value if no data
        }

        return weatherDataList.stream()
                .mapToDouble(WeatherData::getTemperature)
                .max()
                .orElse(Double.MIN_VALUE);
    }

    // Method to calculate the minimum temperature from today's data
    public double calculateMinTemp() {
        List<WeatherData> weatherDataList = getTodayWeatherData();

        if (weatherDataList.isEmpty()) {
            return Double.MAX_VALUE; // Return a default value if no data
        }

        return weatherDataList.stream()
                .mapToDouble(WeatherData::getTemperature)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    // Method to calculate the dominant weather condition (most common weather)
    public String calculateDominantWeather() {
        List<WeatherData> weatherDataList = getTodayWeatherData();
        Map<String, Integer> weatherConditionCount = new HashMap<>();

        for (WeatherData data : weatherDataList) {
            String weather = data.getMainWeather();
            weatherConditionCount.put(weather, weatherConditionCount.getOrDefault(weather, 0) + 1);
        }

        return weatherConditionCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    // Helper method to get today's weather data
    private List<WeatherData> getTodayWeatherData() {
        LocalDate today = LocalDate.now();
        // Fetch today's data from the repository using the date filter
        return weatherRepository.findWeatherByDate(java.sql.Date.valueOf(today));
    }

    public double getAvgTemperature() {
        // Return the average temperature from the fetched data
        return calculateAverageTemp();
    }

    public double getMaxTemperature() {
        // Return the max temperature from the fetched data
        return calculateMaxTemp();
    }

    public double getMinTemperature() {
        // Return the min temperature from the fetched data
        return calculateMinTemp();
    }

    public String getDominantWeather() {
        // Return the most common weather condition
        return calculateDominantWeather();
    }

        public List<HistoricalWeatherData> getHistoricalWeatherData() {
        List<WeatherData> weatherDataList = weatherRepository.findAll(); // Fetch all data from the weather_data table

        // Group the data by date and calculate average temperature
        return weatherDataList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getTimestamp().toLocalDate(), // Group by date
                        Collectors.averagingDouble(WeatherData::getTemperature) // Calculate average temperature for the day
                ))
                .entrySet()
                .stream()
                .map(entry -> new HistoricalWeatherData(entry.getKey(), entry.getValue())) // Convert to your desired output structure
                .collect(Collectors.toList());
    }

}
