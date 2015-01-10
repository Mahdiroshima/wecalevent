/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Mert
 */
@RequestScoped
@ManagedBean
public class UpcomingEventsView {
    @EJB
    private sessionBeanRemote ejb;
    
    private String user_id;
    private List<Event> events = null;
    private User viewUser = null;
    private ScheduleModel eventModel;

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
    
    /**
     * Creates a new instance of UpcomingEventsView
     */
    public UpcomingEventsView() {
    }
    
    @PostConstruct
    public void updateEventList() {
        if (ejb != null) {
            viewUser = ejb.getUser();
            if (user_id == null) {
                events = ejb.getEventsOfUser(viewUser.getUserId());
            } else {
                int id = Integer.parseInt(user_id);
                events = ejb.getEventsOfUser(id);
            }
            eventModel = new DefaultScheduleModel();
            for (Event event : events) {
                ScheduleEvent se = new DefaultScheduleEvent(event.getEventName(), event.getStartingDate(), event.getEndingDate());
                eventModel.addEvent(se);
            }
            RequestContext.getCurrentInstance().update(":schedule");
        }
    }
}
