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
import com.se2.wecalevent.util.XLSReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Mert
 */
@ManagedBean
@SessionScoped
public class ExportImportController implements Serializable {

    /**
     * Creates a new instance of ExportImportController
     */
    public ExportImportController() {
    }

    @EJB
    private sessionBeanRemote ejb;
    private List<Event> myEvents;
    private List<Event> importedEvents;
    private boolean[] result;
    private boolean alreadyImported = false;

    public String exportImport() {
        return "exportImport.xhtml?faces-redirect=true";
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public boolean isAlreadyImported() {
        return alreadyImported;
    }

    public void setAlreadyImported(boolean alreadyImported) {
        this.alreadyImported = alreadyImported;
    }

    public List<Event> getImportedEvents() {
        return importedEvents;
    }

    public void setImportedEvents(List<Event> importedEvents) {
        this.importedEvents = importedEvents;
    }

    public boolean[] getResult() {
        return result;
    }

    public void setResult(boolean[] result) {
        this.result = result;
    }

    
    public void init() {
        if (ejb != null) {
            int user_id = ejb.getUser().getUserId();
            myEvents = ejb.getEventsOfUser(user_id);
        }
    }

    public void exportEvents() {

    }

    public void importEvents() {
        if (importedEvents != null) {
            result = new boolean[importedEvents.size()];
            int i = 0;
            for (Event event : importedEvents) {
                boolean flag = true;
                if (WeatherAPI.isCityExists(event.getLocationCity())) {
                    try {
                        flag = ejb.createEvent(event.getEventName(), event.getEventDescription(), event.getEventType(),
                                event.getDesiredWeather(), event.getVisibility(), event.getLocationCity(),
                                event.getStartingDate(), event.getEndingDate(), new ArrayList<User>());
                    } catch (DateException ex) {
                        Logger.getLogger(ExportImportController.class.getName()).log(Level.SEVERE, null, ex);
                        flag = false;
                    }
                } else {
                    flag = false;
                }
                result[i++] = flag;
            }
            alreadyImported = true;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            alreadyImported = false;
            result = null;
            importedEvents = XLSReader.readXLS(event.getFile().getInputstream());
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException ex) {
            Logger.getLogger(ExportImportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkStatus(Event event) {
        int index = 0;
        for (Event theEvent: importedEvents) {
            if (theEvent.getEventName().equals(event.getEventName()) &&
                    theEvent.getEventDescription().equals(event.getEventDescription()) &&
                            theEvent.getLocationCity().equals(event.getLocationCity())) {
                return result[index];
            }
            index++;
        }
        return false;
    }

}
