/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import com.se2.wecalevent.entities.Weather;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author Mehdi
 */
public class WeatherAPI {
    /**
     * Sample call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=London&mode=xml&units=metric&cnt=16
     */
    
    /**
     * base url of 3 hour based weather forecast
     */
    private static final String baseUrl3hour = "http://api.openweathermap.org/data/2.5/forecast?";
    
    /**
     * * base url of daily based weather forecast
     */
    private static final String baseUrl1day = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    /**
     * This method checks with the weather api whether the given city exists or not
     * @param city City name
     * @return true if the city exists, false otherwise
     */
    public static boolean isCityExists(String city) {
        String query = baseUrl1day + "q=" + city + "&mode=xml";
        Document document = connect(query);
        return WeatherXMLParser.isCityExists(document);
    }
    /**
     * This method gets the necessary weather information from the API
     * @param date The date on which the weather forecast is requested
     * @param city City Name
     * @return the weather forecast estimation
     */
    public static Weather getWeatherForecast(Date date, String city) {
        Date nowDate = new Date();
        long differenceInMs = date.getTime() - nowDate.getTime();
        //convert milliseconds to days
        int days = (int)differenceInMs / (1000 * 60 * 60 * 24);
        String query = "";
        Document document = null;
        String forecast = "";
        Weather weather = new Weather(null, date, city);
        /**
         * This if statement never supposed to work , putted to prevent unwanted consequences
         */
        if (days < 0) {
            query = baseUrl1day + "q=" + city + "&mode=xml";
            document = connect(query);
            forecast = WeatherXMLParser.getForecastFromDailyXML(document, nowDate);
        } else if (days < 5) { // get 3-hour base forecast
            query = baseUrl3hour + "q=" + city + "&mode=xml";
            document = connect(query);
            forecast = WeatherXMLParser.getForecastFromDailyXML(document, date);
        } else if (days < 16){ // get daily forecast
            query = baseUrl1day + "q=" + city + "&mode=xml&cnt=16";
            document = connect(query);
            forecast = WeatherXMLParser.getForecastFromDailyXML(document, date);
        } else {
            forecast = "unknown";
        }
        weather.setWeatherCondition(forecast);
        return weather;
    }
    
    
    /**
     * This method updates the forecast data for given weather object
     * @param weather the object that is going to be updated
     * @return true if the forecast has been changed, false otherwise
     */
    public static boolean updateForecast(Weather weather){
       //TODO: needs to be implemented
       return false; 
    }
    
    
    private static Document connect(String url) {
        //apache http client library element initializations
        HttpClient http = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        try {
            //connects to api and gets the results
            response = http.execute(httpget);
        } catch (IOException ex) {
            Logger.getLogger(WeatherAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Start reading the result
        InputStream contentStream = null;
        String responseBody = "";
        try {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null) {
                throw new IOException(
                        String.format("Unable to get a response from server"));
            }
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 && statusCode >= 300) {
                throw new IOException(
                        String.format("Server responded with status code %d: %s", statusCode, statusLine));
            }
            // The data is good now read the response content
            HttpEntity responseEntity = response.getEntity();
            contentStream = responseEntity.getContent();
            Reader isReader = new InputStreamReader(contentStream);
            int contentSize = (int) responseEntity.getContentLength();
            if (contentSize < 0) {
                contentSize = 8 * 1024;
            }
            StringWriter strWriter = new StringWriter(contentSize);
            char[] buffer = new char[8 * 1024];
            int n = 0;
            while ((n = isReader.read(buffer)) != -1) {
                strWriter.write(buffer, 0, n);
            }
            //flush string
            responseBody = strWriter.toString();
            contentStream.close();
        } catch (IOException e) {
        } catch (RuntimeException re) {
            httpget.abort();
        } finally {
            if (contentStream != null) {
            }
        }
        // start building the xml document object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseBody)));
            return document;
        } catch (Exception e) {
        }
        return null;
    }
    
    
}
