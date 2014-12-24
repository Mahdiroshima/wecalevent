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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
 * @author Mert Ergun <mert.rgun@gmail.com>
 */
public class SendMailTLS {
    
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
    
    public static void send() {
        /**
         * Application admin e-mail configurations
         */
        final String username = "wecalevent@gmail.com";
        final String password = "k2l2s0c_*sf";

        /**
         * GMail SSL configurations
         */
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Create a session with the above creditiantials
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create the message and its properties
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            // Send the message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ParseException {
        String url = "http://api.openweathermap.org/data/2.5/forecast/?q=milano&mode=xml&units=metric";
        Document document = connect(url);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2014-12-27 11:25:25");
        System.out.println(WeatherXMLParser.getForecastFromDailyXML(document, date));
    }
}
