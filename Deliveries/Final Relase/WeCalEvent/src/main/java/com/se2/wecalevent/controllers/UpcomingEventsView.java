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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Mert
 */
@ViewScoped
@ManagedBean
public class UpcomingEventsView {

    @EJB
    private sessionBeanRemote ejb;

    private String user_id;
    private List<Event> events = null;
    private User viewUser = null;
    private ScheduleModel eventModel;
    private Event newParticipantEvent = null;
    private Properties eventDictionary = new Properties();

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public User getViewUser() {
        return viewUser;
    }

    public void setViewUser(User viewUser) {
        this.viewUser = viewUser;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        updateEventList();
    }

    public Event getNewParticipantEvent() {
        return newParticipantEvent;
    }
    /**
     * if new event is added add it to the calendar
     * @param newParticipantEvent 
     */
    public void setNewParticipantEvent(Event newParticipantEvent) {
        this.newParticipantEvent = newParticipantEvent;
        //if new event should be added and if I am viewing the calendar of myself
        if (newParticipantEvent != null && viewUser != null && viewUser.getUserId().equals(ejb.getUser().getUserId())) {
            if (events != null) { //if the event is not already included
                updateEventList();
                RequestContext.getCurrentInstance().update(":schedule");
            }
        }
    }

    /**
     * Creates a new instance of UpcomingEventsView
     */
    public UpcomingEventsView() {
    }
    /**
     * initializes the list of events for the calendar
     */
    @PostConstruct
    public void updateEventList() {
        boolean viewSomeOneElse = false;
        if (ejb != null) {
            //view your events
            if (user_id == null) {
                viewUser = ejb.getUser();
                if (viewUser != null) {
                    events = ejb.getEventsOfUser(viewUser.getUserId());
                } else {
                    return;
                }
            } else { //view someoneelse's calendar
                //TODO PARSE EXCEPTION MAY OCCUR HERE
                int id = Integer.parseInt(user_id);
                viewUser = ejb.getUserById(id);
                events = ejb.getEventsOfUser(id);
                viewSomeOneElse = !viewUser.getUserId().equals(ejb.getUser().getUserId());
            }
            eventModel = new DefaultScheduleModel();
            /**
             * Manage event privacy here
             */
            for (Event event : events) {
                if (viewSomeOneElse) {
                    //If the calendar is public, all users are able to see the calendar content with details
                    if (viewUser.getCalendar().contains("public")) {
                        if (event.getVisibility().contains("public")) {
                            ScheduleEvent se = new DefaultScheduleEvent(event.getEventName(), event.getStartingDate(), event.getEndingDate());
                            eventModel.addEvent(se);
                            eventDictionary.put(se.getId(), event);
                        } else { //unless the event is private TODO: and I am not a participant
                            ScheduleEvent se = new DefaultScheduleEvent("Private Event", event.getStartingDate(), event.getEndingDate());
                            eventModel.addEvent(se);
                            eventDictionary.put(se.getId(), event);
                        }
                    } else { // The calendar is private, you cannot see the details
                        ScheduleEvent se = new DefaultScheduleEvent("Private", event.getStartingDate(), event.getEndingDate());
                        eventModel.addEvent(se);
                        eventDictionary.put(se.getId(), event);
                        
                    }
                } else { // I view my calendar, show me everything
                    ScheduleEvent se = new DefaultScheduleEvent(event.getEventName(), event.getStartingDate(), event.getEndingDate());
                    eventModel.addEvent(se);
                    eventDictionary.put(se.getId(), event);
                }
            }
            RequestContext.getCurrentInstance().update(":schedule");
        }
    }
    /**
     * handles the click event on the calendar view
     * and redirect to related page after click
     * @param selectEvent 
     */
    public void onEventSelect(SelectEvent selectEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
        Event selectedEvent = (Event)eventDictionary.get(event.getId());
        FacesMessage message = null;
        if (selectedEvent == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Internal error","Event is no more");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            try {
                Date now = new Date();
                //if the event is in the past, show a message
                if (selectedEvent.getStartingDate().compareTo(now) < 0) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Internal error", "Event has passed");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("viewEvent.xhtml?id=" + selectedEvent.getEventId());
                }
            } catch (IOException ex) {
                Logger.getLogger(UpcomingEventsView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
