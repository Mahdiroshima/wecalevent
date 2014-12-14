/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import com.se2.wecalevent.entities.Weather;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static boolean isCityExists(String city) {
        try {
            OwmClient owm = new OwmClient();
            WeatherStatusResponse currentWeather = owm.currentWeatherAtCity(city);
            return (currentWeather.hasWeatherStatus());
        } catch (IOException ex) {
            Logger.getLogger(WeatherAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(WeatherAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static String getWeatherForecast(Date date, String city) {
        
        return "";
    }
    
    
    /**
     * This method updates the forecast data for given weather object
     * @param weather the object that is going to be updated
     * @return true if the forecast has been changed, false otherwise
     */
    public static boolean updateForecast(Weather weather){
       return false; 
    }
    
    
}
