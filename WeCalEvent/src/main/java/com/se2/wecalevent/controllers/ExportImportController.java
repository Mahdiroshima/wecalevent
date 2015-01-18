/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Mert
 */
@Named(value = "exportImportController")
@RequestScoped
public class ExportImportController {

    /**
     * Creates a new instance of ExportImportController
     */
    public ExportImportController() {
    }
    
    @EJB
    private sessionBeanRemote ejb;
    private List<Event> myEvents; 
    
    
    public String exportImport() {
        return "exportImport.xhtml?faces-redirect=true";
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }
    
    @PostConstruct
    public void init() {
        if (ejb!= null) {
            int user_id = ejb.getUser().getUserId();
            myEvents = ejb.getEventsOfUser(user_id);
        }
    }
    
    public void exportEvents() {
        
    }
    
    public void importEvents() {
    
    }
    
}
