/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        NodeList nList = document.getDocumentElement().getElementsByTagName("time");
        int temp = 0;
        String ch = "";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Datetosearch = formatter.format(date);
        do {
            Node nNode = nList.item(temp);
            int x = nNode.getNodeValue().compareTo(Datetosearch);
            if (x == 0) {
                Element eElement = (Element) nNode;
                String s = eElement.getElementsByTagName("symbol").item(temp).getAttributes().getNamedItem("name").getNodeValue();
                if (s.contains("cloud")) {
                    ch = "cloudy";
                } else if ((s.contains("clear"))) {
                    ch = "sunny";
                } else if ((s.contains("rain"))) {
                    ch = "Rainy";
                } else {
                    ch = "Snowy";
                }

            } else {
                ch = "Uknown";
            }

            temp++;

        } while (nList.getLength() == temp);

        return ch;

    }
    }
    
    public static String getForecastFrom3hourlyXML(Document document, Date date) {
        //TODO: need to be implemented
        return "cloudy";
    }
}
