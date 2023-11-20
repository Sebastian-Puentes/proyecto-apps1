package com.example.parcial;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;



import androidx.appcompat.app.AppCompatActivity;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        ForecastData[] forecastData = getForecastData();

        LinearLayout forecastLayout = findViewById(R.id.forecastLayout);

        for (ForecastData data : forecastData) {
            LinearLayout forecastItem = (LinearLayout) getLayoutInflater().inflate(R.layout.forecast_item, null);

            TextView dateTextView = forecastItem.findViewById(R.id.textViewDate);
            TextView temperatureTextView = forecastItem.findViewById(R.id.textViewTemperature);
            TextView descriptionTextView = forecastItem.findViewById(R.id.textViewDescription);

            dateTextView.setText(data.getDate());
            temperatureTextView.setText(data.getTemperature());
            descriptionTextView.setText(data.getDescription());

            forecastLayout.addView(forecastItem);
        }
    }

    private ForecastData[] getForecastData() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

        String[] descriptions = {"Cielo Despejado", "Nublado", "Lluvia Ligera", "Tormentas", "Granizo"};

        ForecastData[] forecastData = new ForecastData[5];

        for (int i = 0; i < 5; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();

            String formattedDate = dateFormat.format(date);

            String temperature = (15 - i) + "°C";
            String description = descriptions[i];

            forecastData[i] = new ForecastData(getDayOfWeek(i), formattedDate, temperature, description);
        }

        return forecastData;
    }


    private String getDayOfWeek(int daysLater) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysLater);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}
