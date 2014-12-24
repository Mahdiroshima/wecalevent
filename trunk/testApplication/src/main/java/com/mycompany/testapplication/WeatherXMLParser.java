/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Mert Ergun <mert.rgun@gmail.com>
 */
public class WeatherXMLParser {


    public static String getForecastFromDailyXML(Document document, Date date) {
        //TODO: need to be implemented
        NodeList nList = document.getDocumentElement().getElementsByTagName("time");
        int temp = 0;
        String ch = "";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String Datetosearch = formatter.format(date);
        Format formatter1 = new SimpleDateFormat("HH:mm:ss");
        String timetosearch = formatter1.format(date);
        System.out.println(Datetosearch);
        System.out.println(timetosearch);
        do {
            Node nNode = nList.item(temp);
            boolean x = nNode.getAttributes().getNamedItem("from").getNodeValue().contains(Datetosearch);
            System.out.println(nNode.getAttributes().getNamedItem("from").getNodeValue());
            if (x == true) {
                Element eElement = (Element) nNode;
                System.out.println("test");
                String s = eElement.getElementsByTagName("symbol").item(0).getAttributes().getNamedItem("name").getNodeValue();
                if (s.contains("cloud")) {
                    ch = "cloudy";
                } else if ((s.contains("clear"))) {
                    ch = "sunny";
                } else if ((s.contains("rain"))) {
                    ch = "rainy";
                } else {
                    ch = "snowy";
                }
break;
            } else {
                ch = "uknown";
            }

            temp++;

        } while (nList.getLength() > temp) ;

        return ch;
    }

}
