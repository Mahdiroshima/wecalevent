/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.remote.sessionBeanRemote;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mehdi
 */
@ManagedBean
@RequestScoped
public class EventCreationView {
     @EJB
    private sessionBeanRemote ejb;
     
  
    private String eventName = "Studying";
    private String eventDescription = "Nazrin will kill us if we don't wake up at 9";
    private String eventType = "indoor";
    private String desiredWeather = "rainy";
    private String visibility = "private"; 
    private String locationCity = "milAn".toLowerCase();
    private Date startingDate;
    private Date endingDate;
   

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDesiredWeather() {
        return desiredWeather;
    }

    public void setDesiredWeather(String desiredWeather) {
        this.desiredWeather = desiredWeather;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }
    
    public String submit() {
        boolean status = ejb.createevent(eventName, eventDescription, eventType, desiredWeather, visibility, locationCity, startingDate, endingDate);
        FacesMessage message = null;
        if (status) {
            message = new FacesMessage("Hurry !!", "Your event have been created");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "home.xhtml";
        } else {
            message = new FacesMessage("Sorry", "You event cannot be created :( ");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "eventcreation.xhtml";
        }
}
}
