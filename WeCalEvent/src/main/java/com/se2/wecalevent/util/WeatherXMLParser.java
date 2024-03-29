/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import java.text.Format;
import java.text.ParseException;
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
     * This function parses the document and checks whether the city name is
     * valid or not
     *
     * @param document
     * @return true if city exists, false otherwise
     */
    public static boolean isCityExists(Document document) {
        //if the parsed document is null that means, an xml file is not 
        //generated and therfore the ctiy does not exists
        if (document == null) {
            return false;
        } else {
            return true;
        }
    }
/**
 * This method to get forecast from daily XML file
 * 
 * @param document
 * @param date
 * @return 
 */
    public static String getForecastFromDailyXML(Document document, Date date) {
        if (document == null) {
            return "unknown";
        }
        NodeList nList = document.getDocumentElement().getElementsByTagName("time");
        int temp = 0;
        String ch = "";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Datetosearch = formatter.format(date);
        do {
            Node nNode = nList.item(temp);
            boolean x = nNode.getAttributes().getNamedItem("day").getNodeValue().contains(Datetosearch);
            if (x == true) {
                Element eElement = (Element) nNode;
                String s = eElement.getElementsByTagName("symbol").item(0).getAttributes().getNamedItem("name").getNodeValue();
                if (s.contains("cloud")) {
                    ch = "cloudy";
                } else if ((s.contains("clear"))) {
                    ch = "clear";
                } else if ((s.contains("rain"))) {
                    ch = "rainy";
                } else {
                    ch = "snowy";
                }
                break;
            } else {
                ch = "unknown";
            }

            temp++;

        } while (nList.getLength() > temp);

        return ch;
    }
/**
 *  * This method to get forecast from 3hours XML file
 * @param document
 * @param date
 * @return 
 */
    public static String getForecastFrom3hourlyXML(Document document, Date date) {
        if (document == null) return "unknown";
        NodeList nList = document.getDocumentElement().getElementsByTagName("time");
        int temp = 0;
        String ch = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        do {
            try {
                Node nNode = nList.item(temp);
                String x = nNode.getAttributes().getNamedItem("from").getNodeValue().replace("T", " ");
                String y = nNode.getAttributes().getNamedItem("to").getNodeValue().replace("T", " ");
                Date fromdate = formatter.parse(x);
                Date todate = formatter.parse(y);
                int comp = fromdate.compareTo(date);
                int comp1 = todate.compareTo(date);
                if (comp<=0 && comp1>0 ) {
                    Element eElement = (Element) nNode;
                    String s = eElement.getElementsByTagName("symbol").item(0).getAttributes().getNamedItem("name").getNodeValue();
                    if (s.contains("cloud")) {
                        ch = "cloudy";
                    } else if ((s.contains("clear"))) {
                        ch = "clear";
                    } else if ((s.contains("rain"))) {
                        ch = "rainy";
                    } else {
                        ch = "snowy";
                    }
                    break;
                } else {
                    ch = "unknown";
                }

                temp++;
            } catch (ParseException ex) {
            }

        } while (nList.getLength() > temp);

        return ch;
    }

}
