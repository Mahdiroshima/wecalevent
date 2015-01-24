/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mehdi
 * @author Mert
 */
@ManagedBean
@RequestScoped
public class EventViewController implements Serializable {

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
    private String[] selectedWeather;
    private String event_id;
    private List<User> peopleAlreadyParticipate = new ArrayList<User>();

    public List<User> getPeopleAlreadyParticipate() {
        return peopleAlreadyParticipate;
    }

    public void setPeopleAlreadyParticipate(List<User> peopleAlreadyParticipate) {
        this.peopleAlreadyParticipate = peopleAlreadyParticipate;
    }

    public UserLoginView getUserLoginView() {
        return userLoginView;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setUserLoginView(UserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }

    public String[] getSelectedWeather() {
        return selectedWeather;
    }

    public void setSelectedWeather(String[] selectedWeather) {
        this.selectedWeather = selectedWeather;
        desiredWeather = "";
        for (String s : selectedWeather) {
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

    public void init() {
        if (event_id != null && ejb != null && ejb.getUser() != null) {
            int eid = Integer.parseInt(event_id);
            Event theEvent = ejb.getEventById(eid);
            peopleAlreadyParticipate = ejb.getParticipantsOfEvent(eid);
            this.eventName = theEvent.getEventName();
            this.eventDescription = theEvent.getEventDescription();
            this.eventType = theEvent.getEventType();
            this.desiredWeather = theEvent.getDesiredWeather();
            this.visibility = theEvent.getVisibility();
            this.locationCity = theEvent.getLocationCity();
            this.startingDate = theEvent.getStartingDate();
            this.endingDate = theEvent.getEndingDate();
            this.selectedWeather = theEvent.getDesiredWeather().split("-");
        }
    }

    private List<User> getListOfSelectedUsers() {
        List<User> userList = new ArrayList<User>();
        List<String> selList = userLoginView.getSelectedPeople();
        if (selList != null) {
            for (String s : selList) {
                int id = Integer.parseInt(s);
                for (User user : userLoginView.getPeople()) {
                    if (id == user.getUserId()) {
                        userList.add(user);
                        break;
                    }
                }
            }
        }
        return userList;
    }

    public void deleteEvent() {
        User user = ejb.getUserById(ejb.getUser().getUserId());
        List<Event> events = user.getEventList2();
        boolean flag = false;
        int eid = Integer.parseInt(event_id);
        for (Event theEvent : events) {
            if (theEvent.getEventId().equals(eid)) {
                flag = true;
                break;
            }
        }
        try {
            if (!flag) {
                FacesMessage message = new FacesMessage("Sorry", "You are not authorized to delate this event");
                FacesContext.getCurrentInstance().addMessage(null, message);
                FacesContext.getCurrentInstance().getExternalContext().redirect("viewEvent.xhtml?id=" + eid);
            } else {
                ejb.removeEntity(eid, Event.class);
                FacesMessage message = new FacesMessage("Your event had been delated");
                FacesContext.getCurrentInstance().addMessage(null, message);
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
            }
        } catch (IOException ex) {
            Logger.getLogger(EventViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String updateEvent() {
        User user = ejb.getUserById(ejb.getUser().getUserId());
        List<Event> events = user.getEventList2();
        boolean flag = false;
        int eid = Integer.parseInt(event_id);
        for (Event theEvent : events) {
            if (theEvent.getEventId().equals(eid)) {
                flag = true;
                break;
            }
        }
        if (flag) { // I can edit the event
            return "updateEvent.xhtml?id=" + event_id + "&faces-redirect=true";
        } else {
            FacesMessage message = new FacesMessage("Sorry", "You are not authorized to update this event");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "viewEvent.xhtml?id=" + eid + "&faces-redirect=true";
        }
    }

    public void check() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (event_id == null) {
            try {
                System.out.println("Error comes from here");
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(EventViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            User user = ejb.getUserById(ejb.getUser().getUserId());
            boolean flag = false;
            boolean isPrivate = this.visibility.contains("private");
            for (User oneuser : peopleAlreadyParticipate) {
                if (oneuser.getUserId().equals(user.getUserId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag && isPrivate) {
                System.out.println("Error comes from or here: flag: " + flag + " isPrivate: " + isPrivate);
                FacesMessage message = new FacesMessage("Sorry", "You are not authorized to view this event");
                FacesContext.getCurrentInstance().addMessage(null, message);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(EventViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String viewUser(Integer user_id) {
        return "home.xhtml?id=" + user_id + "&faces-redirect=true";
    }
}
