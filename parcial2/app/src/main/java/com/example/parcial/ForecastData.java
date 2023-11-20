package com.example.parcial;

public class ForecastData {
    private String day;
    private String date;
    private String temperature;
    private String description;

    public ForecastData(String day, String date, String temperature, String description) {
        this.day = day;
        this.date = date;
        this.temperature = temperature;
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ForecastData{" +
                "day='" + day + '\'' +
                ", date='" + date + '\'' +
                ", temperature='" + temperature + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

