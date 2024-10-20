package com.ex.weather_monitoring.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.weather_monitoring.model.DailySummary;
import java.util.*;;

public interface DailySummaryRepository  extends JpaRepository<DailySummary, Long> {
    List<DailySummary> findByCityAndDate(String city, LocalDate date);
    
}
