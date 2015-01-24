/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import com.se2.wecalevent.util.DateException;
import com.se2.wecalevent.util.WeatherAPI;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mehdi
 */
@ManagedBean
@ViewScoped
public class EventCreationView implements Serializable {

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
        if (this.event_id == null || !this.event_id.equals(event_id)) {
            this.event_id = event_id;
            init();
        }
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

    @PostConstruct
    public void init() {
        //This is when a user wants to create an event
        if (event_id == null && ejb != null && ejb.getUser() != null && userLoginView.getPeople() == null) {
            int uid = ejb.getUser().getUserId();
            List<Event> events = ejb.getEventsOfUser(uid);
            List<User> people = ejb.getAllUsers();
            //Remove myself from the partipant list
            int myIndex = 0;
            for (User oneUser : people) {
                if (oneUser.getUserId().equals(uid)) {
                    break;
                }
                myIndex++;
            }
            people.remove(myIndex);
            userLoginView.setPeople(people);
            userLoginView.setSelectedPeople(new ArrayList<String>());
        } //This is the case when a user view an event or edit one
        else if (event_id != null && ejb != null && ejb.getUser() != null) {
            int uid = ejb.getUser().getUserId();
            List<Event> events = ejb.getEventsOfUser(uid);
            int eid = Integer.parseInt(event_id);
            List<User> people = ejb.getAllUsers();
            peopleAlreadyParticipate = ejb.getParticipantsOfEvent(eid);
            int sizePeople = people.size();
            int sizeParticipate = peopleAlreadyParticipate.size();
            for (int i = 0; i < sizePeople; i++) {
                User currentOne = people.get(i);
                for (int j = 0; j < sizeParticipate; j++) {
                    if (currentOne.getUserId().equals(peopleAlreadyParticipate.get(j).getUserId())) {
                        people.remove(i);
                        i--;
                        sizePeople--;
                        break;
                    }
                }
            }
            userLoginView.setPeople(people);
            userLoginView.setSelectedPeople(new ArrayList<String>());
            System.out.println("I've reset the selected list");
            for (Event event : events) {
                if (event.getEventId() == eid) {
                    this.eventName = event.getEventName();
                    this.eventDescription = event.getEventDescription();
                    this.eventType = event.getEventType();
                    this.desiredWeather = event.getDesiredWeather();
                    this.visibility = event.getVisibility();
                    this.locationCity = event.getLocationCity();
                    this.startingDate = event.getStartingDate();
                    this.endingDate = event.getEndingDate();
                    this.selectedWeather = event.getDesiredWeather().split("-");
                    break;
                }
            }

        }
    }

    public void viewPeople() {
        RequestContext.getCurrentInstance().openDialog("dialogs/invitePeople");
    }

    public void save() {
        RequestContext.getCurrentInstance().closeDialog("dialogs/invitePeople");
    }

    public String submit() {
        try {
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
        } catch (DateException ex) {
            FacesMessage message = new FacesMessage("Sorry", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "eventcreation.xhtml";
        }
    }

    public String update() {
        int eid = Integer.parseInt(event_id);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            boolean status = ejb.updateEvent(eid, eventName, eventDescription, eventType, desiredWeather, visibility, locationCity, startingDate, endingDate, getListOfSelectedUsers());
            FacesMessage message = null;
            if (status) {
                userLoginView.setPeople(null);
                message = new FacesMessage("Your event have been updated ");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "home.xhtml?faces-redirect=true";
            } else {
                message = new FacesMessage("Sorry, Your event is not updated :( ");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "updateEvent.xhtml?id=" + eid;
            }
        } catch (DateException ex) {
            FacesMessage message = new FacesMessage("Sorry", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "updateEvent.xhtml?id=" + eid;
        }
    }

    public void handleKeyEvent() {
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (locationCity == null) {
            return;
        }
        FacesMessage message = null;
        boolean status = WeatherAPI.isCityExists(locationCity);
        if (status) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", locationCity + " is OK.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", locationCity + " doesn't exist, try again");
            locationCity = "";
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public void check() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        if (event_id == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        } else {
            User user = ejb.getUserById(ejb.getUser().getUserId());
            List<Event> events = user.getEventList2();
            boolean flag = false;
            int eid = Integer.parseInt(event_id);
            for (Event event : events) {
                if (event.getEventId().equals(eid)) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                FacesMessage message = new FacesMessage("Sorry", "You are not authorized to update this event");
                FacesContext.getCurrentInstance().addMessage(null, message);
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
            }
        }
    }
}
