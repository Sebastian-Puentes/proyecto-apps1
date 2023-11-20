package com.example.parcial;
import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("name")
    private String cityName;

    @SerializedName("main")
    private MainInfo mainInfo;

    @SerializedName("weather")
    private WeatherInfo[] weatherInfo;


    public String getCityName() {
        return cityName;
    }

    public MainInfo getMainInfo() {
        return mainInfo;
    }

    public WeatherInfo[] getWeatherInfo() {
        return weatherInfo;
    }
}
