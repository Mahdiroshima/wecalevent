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

    public static boolean isCityExists(String city) {
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
    
    
    public static Document connect(String url) {
        HttpClient http = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = http.execute(httpget);
        } catch (IOException ex) {
            Logger.getLogger(WeatherAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream contentStream = null;
        String responseBody = "";
        try {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null) {
                throw new IOException(
                        String.format("Unable to get a response from OWM server"));
            }
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 && statusCode >= 300) {
                throw new IOException(
                        String.format("OWM server responded with status code %d: %s", statusCode, statusLine));
            }
            /* Read the response content */
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
            responseBody = strWriter.toString();
            contentStream.close();
        } catch (IOException e) {
        } catch (RuntimeException re) {
            httpget.abort();
        } finally {
            if (contentStream != null) {
            }
        }
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
