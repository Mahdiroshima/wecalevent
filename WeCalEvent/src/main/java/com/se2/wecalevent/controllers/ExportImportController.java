/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.remote.sessionBeanRemote;
import com.se2.wecalevent.util.XLSReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

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
    private List<Event> importedEvents;
    
    
    public String exportImport() {
        return "exportImport.xhtml?faces-redirect=true";
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public List<Event> getImportedEvents() {
        return importedEvents;
    }

    public void setImportedEvents(List<Event> importedEvents) {
        this.importedEvents = importedEvents;
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
    
    public void handleFileUpload(FileUploadEvent event) {
        try {
            importedEvents = XLSReader.readXLS(event.getFile().getInputstream());
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException ex) {
            Logger.getLogger(ExportImportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
