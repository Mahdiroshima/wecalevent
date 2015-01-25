/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import com.se2.wecalevent.entities.Weather;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author Mehdi
 */
public class WeatherAPITest extends TestCase {

    public WeatherAPITest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of isCityExists method, of class WeatherAPI. Empty city input
     */
    public void testIsCityExistsEmpty() {
        System.out.println("isCityExists");
        String city = "";
        boolean expResult = false;
        boolean result = WeatherAPI.isCityExists(city);
        assertEquals(expResult, result);
    }

    /**
     * Test of isCityExists method, of class WeatherAPI. Wrong city input
     */
    public void testIsCityExistsWrongInput() {
        System.out.println("isCityExists");
        String city = "thisiswrong";
        boolean expResult = false;
        boolean result = WeatherAPI.isCityExists(city);
        assertEquals(expResult, result);
    }

    /**
     * Test of isCityExists method, of class WeatherAPI. Correct city input
     */
    public void testIsCityExistsCorrectInput() {
        System.out.println("isCityExists");
        String city = "Miami";
        boolean expResult = true;
        boolean result = WeatherAPI.isCityExists(city);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWeatherForecast method, of class WeatherAPI. Empty city input
     */
    public void testGetWeatherForecastEmpty() {
        System.out.println("getWeatherForecastEmpty");
        Date date = new Date();
        String city = "";
        Weather expResult = null;
        Weather result = WeatherAPI.getWeatherForecast(date, city);
        String[] possibilities = new String[]{"Unknown", "Sunny", "Cloudy", "Rainy", "Snowy"};
        boolean flag = false;
        for (String s : possibilities) {
            if (s.equalsIgnoreCase(result.getWeatherCondition())) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getWeatherForecast method, of class WeatherAPI. wrong city input
     */
    public void testGetWeatherForecastWrongInput() {
        System.out.println("getWeatherForecastWrongInput");
        Date date = null;
        String city = "nowaytowork";
        Weather expResult = null;
        Weather result = WeatherAPI.getWeatherForecast(date, city);
        String[] possibilities = new String[]{"Unknown", "Sunny", "Cloudy", "Rainy", "Snowy"};
        boolean flag = false;
        for (String s : possibilities) {
            if (s.equalsIgnoreCase(result.getWeatherCondition())) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getWeatherForecast method, of class WeatherAPI. Empty city input
     */
    public void testGetWeatherForecastCorrect() {
        System.out.println("getWeatherForecastCorrect");
        Date date = null;
        String city = "Milan";
        Weather expResult = null;
        Weather result = WeatherAPI.getWeatherForecast(date, city);
        String[] possibilities = new String[]{"Unknown", "Sunny", "Cloudy", "Rainy", "Snowy"};
        boolean flag = false;
        for (String s : possibilities) {
            if (s.equalsIgnoreCase(result.getWeatherCondition())) {
                flag = true;
                break;
            }
        }
        assertEquals(true, flag);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of updateForecast method, of class WeatherAPI.
     */
    public void testUpdateForecast() {
        System.out.println("updateForecast");
        Weather weather = null;
        boolean expResult = false;
        boolean result = WeatherAPI.updateForecast(weather);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

}
