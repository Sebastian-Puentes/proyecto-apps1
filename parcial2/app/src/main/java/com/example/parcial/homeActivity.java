package com.example.parcial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import com.google.android.gms.tasks.OnSuccessListener;
import android.util.Log;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import android.view.View;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    private TextView textViewCity, textViewTemperature, textViewDescription;
    private FusedLocationProviderClient fusedLocationClient;
    private ImageView weatherIcon, temperatureIcon, descriptionIcon;



    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewCity = findViewById(R.id.textViewCity);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewDescription = findViewById(R.id.textViewDescription);
        weatherIcon = findViewById(R.id.weatherIcon);
        temperatureIcon = findViewById(R.id.temperatureIcon);
        descriptionIcon = findViewById(R.id.descriptionIcon);

        auth= FirebaseAuth.getInstance();
        button= findViewById(R.id.logout);
        user= auth.getCurrentUser();


        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> updateWeather());

        Button forecastButton = findViewById(R.id.forecastButton);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, ForecastActivity.class);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateWeather();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateWeather();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }


    private void showPermissionDeniedDialog() {

    }


    // ...

    private void updateWeather() {
        clearWeatherInfo();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateWeatherData(location.getLatitude(), location.getLongitude());
                    } else {
                        requestLocationUpdates();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(LocationRequest.create(), new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    fusedLocationClient.removeLocationUpdates(this);
                    Location updatedLocation = locationResult.getLastLocation();
                    if (updatedLocation != null) {
                        updateWeatherData(updatedLocation.getLatitude(), updatedLocation.getLongitude());
                    } else {

                    }
                }
            }, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    private void clearWeatherInfo() {
        textViewCity.setText("");
        textViewTemperature.setText("");
        textViewDescription.setText("");
    }




    private void updateWeatherData(double latitude, double longitude) {
        String apiKey = "05aaf8a9d5431644cd7fb4de9f0c1540";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processWeatherData(responseData);
                        }
                    });
                }
            }
        });
    }



    private String translateDescription(String englishDescription) {
        switch (englishDescription.toLowerCase()) {
            case "clear sky":
                return "Cielo Despejado";
            case "few clouds":
                return "Algunas Nubes";
            case "scattered clouds":
                return "Nubes Dispersas";
            case "broken clouds":
                return "Nublado";
            case "shower rain":
                return "Lluvia";
            case "light intensity drizzle":
                return "llovizna de intensidad ligera";
            default:
                return englishDescription;
        }
    }

    private void updateUI(String city, double temperature, String weatherDescription) {
        String translatedDescription = translateDescription(weatherDescription);
        updateIcons(translatedDescription);

        textViewCity.setText("Barrio: " + city);
        textViewTemperature.setText("Temperatura: " + temperature + "°C");
        textViewDescription.setText("Descripción: " + translatedDescription);
    }

    private void updateIcons(String description) {
        int weatherIconResource = getWeatherIconResource(description);
        weatherIcon.setImageResource(weatherIconResource);

        int descriptionIconResource = getDescriptionIconResource(description);
        descriptionIcon.setImageResource(descriptionIconResource);
    }
    private int getDescriptionIconResource(String description) {
        switch (description.toLowerCase()) {
            case "cielo despejado":
                return R.drawable.dom;
            case "algunas nubes":
                return R.drawable.tiempo;
            case "nubes dispersas":
                return R.drawable.nubes;
            case "nublado":
                return R.drawable.nublado;
            case "lluvia":
                return R.drawable.lluviamuyfuerte;
            case "llovizna de intensidad ligera":
                return R.drawable.llovizna;
            default:
                return R.drawable.solesito;
        }
    }



    private int getWeatherIconResource(String description) {
        switch (description.toLowerCase()) {
            case "cielo despejado":
                return R.drawable.dom;
            case "algunas nubes":
                return R.drawable.tiempo;
            case "nubes dispersas":
                return R.drawable.nubes;
            case "nublado":
                return R.drawable.nublado;
            case "lluvia":
                return R.drawable.lluviamuyfuerte;
            case "llovizna de intensidad ligera":
                return R.drawable.llovizna;
            default:
                return R.drawable.solesito;
        }
    }
    private void processWeatherData(String jsonData) {
        Gson gson = new Gson();
        WeatherData weatherData = gson.fromJson(jsonData, WeatherData.class);

        String cityName = weatherData.getCityName();
        double temperatureKelvin = weatherData.getMainInfo().getTemperature();
        String description = weatherData.getWeatherInfo()[0].getDescription();

        double temperatureCelsius = temperatureKelvin - 273.15;

        String formattedTemperature = String.format("%.1f", temperatureCelsius);

        String translatedDescription = translateDescription(description);

        updateUI(cityName, Double.parseDouble(formattedTemperature), translatedDescription);
    }


}



