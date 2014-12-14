/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import java.io.IOException;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

/**
 *
 * @author Mehdi
 */
public class WeatherAPI {

    public WeatherAPI() throws IOException, JSONException {
        OwmClient owm = new OwmClient();
        WeatherStatusResponse currentWeather = owm.currentWeatherAtCity("Tokyo", "JP");
        if (currentWeather.hasWeatherStatus()) {
            WeatherData weather = currentWeather.getWeatherStatus().get(0);
            if (weather.getPrecipitation() == Integer.MIN_VALUE) {
                WeatherCondition weatherCondition = weather.  getWeatherConditions().get(0);
                String description = weatherCondition.getDescription();
                if (description.contains("rain") || description.contains("shower")) {
                    System.out.println("No rain measures in Tokyo but reports of " + description);
                } else {
                    System.out.println("No rain measures in Tokyo: " + description);
                }
            } else {
                System.out.println("It's raining in Tokyo: " + weather.getPrecipitation() + " mm/h");
            }
        }
    }

    
}
