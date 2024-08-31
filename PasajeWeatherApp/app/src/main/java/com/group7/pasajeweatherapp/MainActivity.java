package com.group7.pasajeweatherapp;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group7.pasajeweatherapp.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        SearchView searchView = findViewById(R.id.searchView);
        TextView weatherInfo = findViewById(R.id.weatherInfo);
        TextView errorInfo = findViewById(R.id.errorInfo);

        weatherViewModel.getWeather().observe(this, weatherModel -> {
            if (weatherModel != null) {
                String mainWeather = weatherModel.getWeather()[0].getMain();
                String description = weatherModel.getWeather()[0].getDescription();
                double temp = weatherModel.getMain().getTemp();

                weatherInfo.setText(String.format("Temperature: %.2f°C\nMain: %s\nDescription: %s",
                        temp, mainWeather, description));
                errorInfo.setText("");
            }
        });

        weatherViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                errorInfo.setText(error);
                weatherInfo.setText("");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                weatherViewModel.fetchWeather(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
