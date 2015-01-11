/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import com.se2.wecalevent.util.WeatherAPI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mehdi
 */
@ManagedBean
@RequestScoped
public class EventCreationView {

    @EJB
    private sessionBeanRemote ejb;
    
    @ManagedProperty("#{userLoginView}")
    private UserLoginView userLoginView;
    private String eventName;
    private String eventDescription;
    private String eventType;
    private String desiredWeather;
    private String visibility;
    private String locationCity;
    private Date startingDate;
    private Date endingDate;
    private String[] Selectedweather;

    public UserLoginView getUserLoginView() {
        return userLoginView;
    }

    public void setUserLoginView(UserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }
    
    public String[] getSelectedweather() {
        return Selectedweather;
    }

    public void setSelectedweather(String[] Selectedweather) {
        this.Selectedweather = Selectedweather;
        desiredWeather = "";
        for (String s : Selectedweather) {
            desiredWeather += s + "-";
        }
                   
    }
    
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
    
    @PostConstruct
    public void init() {
        if (userLoginView.getPeople() == null) {
            List<User> people = ejb.getAllUsers();
            int index = people.indexOf(ejb.getUser());
            if (index > -1) {
                people.remove(index);
            }
            userLoginView.setPeople(people);
            userLoginView.setSelectedPeople(new ArrayList<String>());
        }
    }
    
    public void viewPeople() {
        RequestContext.getCurrentInstance().openDialog("dialogs/invitePeople");
    }
    
    public void save() {
        RequestContext.getCurrentInstance().closeDialog("dialogs/invitePeople");
    }

    public String submit() {
        boolean status = ejb.createEvent(eventName, eventDescription, eventType, desiredWeather, visibility, locationCity, startingDate, endingDate, getListOfSelectedUsers());
        FacesMessage message = null;
        if (status) {
            userLoginView.setPeople(null);
            message = new FacesMessage("Hurry !!", "Your event have been created");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "home.xhtml?faces-redirect=true";
        } else {
            message = new FacesMessage("Sorry", "You event cannot be created :( ");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "eventcreation.xhtml";
        }
    }
    
    public void handleKeyEvent() {
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (locationCity == null) return;
        FacesMessage message = null;
        boolean status = WeatherAPI.isCityExists(locationCity);
        if (status) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO,"", locationCity + " is OK.");
        }
        else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", locationCity + " doesn't exist, try again");
            locationCity = "";
        }
            
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    private List<User> getListOfSelectedUsers() {
        List<User> userList = new ArrayList<User>();
        for (String s : userLoginView.getSelectedPeople()) {
            int id = Integer.parseInt(s);
            for (User user : userLoginView.getPeople()) {
                if (id == user.getUserId()) {
                    userList.add(user);
                    break;
                }
            }
        }
        return userList;
    }
}
