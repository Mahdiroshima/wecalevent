/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import java.util.Date;
import org.w3c.dom.Document;

/**
 *
 * @author Mert
 */
public class WeatherXMLParser {
    /**
     * This function parses the document and checks whether the city name is valid or not
     * @param document 
     * @return true if city exists, false otherwise
     */
    public static boolean isCityExists(Document document) {
        //if the parsed document is null that means, an xml file is not 
        //generated and therfore the ctiy does not exists
        if (document == null) {
            return false;
        } else {
            //TODO: need to be implemented
            return true;
        }
    }
    
    public static String getForecastFromDailyXML(Document document, Date date) {
        //TODO: need to be implemented
        return "cloudy";
    }
    
    public static String getForecastFrom3hourlyXML(Document document, Date date) {
        //TODO: need to be implemented
        return "cloudy";
    }
}
