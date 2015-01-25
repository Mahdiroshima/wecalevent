/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class handles e-mail sending operations
 * @author Mert Ergun <mert.rgun@gmail.com>
 */
public class EmailAPI {
    private static final String username = "wecalevent@gmail.com";
    private static final String password = "k2l2s0c_*sf";
    /**
     * User notificaiton types
     */
    public enum Email_Type {
        OWNER_NEW_DATE_SUGGESTION,
        PARTICIPANT_FORECAST_CHANGE,
        PARTICIPANT_BAD_WEATHER
    }
    
    /**
     * This method prepares an email with the given parameters and sends it.
     * @param to The receiver e-mails, multiple e-mails can be seperated by comma(,)
     * @param subject The subject of the e-mail
     * @param content The content of the e-mail
     * @return true if the operation is successful, false otherwise
     */
    public static boolean sendEmail(String to, String subject, String content) {
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
        Session session = Session.getInstance(props,
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
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            // Send the message
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
