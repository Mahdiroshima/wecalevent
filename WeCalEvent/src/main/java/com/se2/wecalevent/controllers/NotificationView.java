/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.Notification;
import com.se2.wecalevent.remote.sessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List; 
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mert
 */
@Named(value = "notificationView")
@SessionScoped
public class NotificationView implements Serializable {
    @EJB
    private sessionBeanRemote ejb;
    /**
     * Creates a new instance of NotificationView
     */
    public NotificationView() {
    }
    private Notification selectedNotification;
    private List<Notification> notifications;
    
    
    @PostConstruct
    public void initNotifications() {
        if (ejb != null && ejb.getUser() != null) {
            notifications = ejb.getNotifications();
        }
    }
    
    public void viewNotification(Notification notification) {
        selectedNotification = notification;
        RequestContext.getCurrentInstance().openDialog("dialogs/notifView");
    }
    
    public void acceptInvitation() {
        if (selectedNotification != null) {
            ejb.acceptInvitation(selectedNotification);
        }
        RequestContext.getCurrentInstance().closeDialog(selectedNotification.getRelatedTo());
        notifications.remove(selectedNotification);
        selectedNotification = null;
        RequestContext.getCurrentInstance().update(":notifPanell");
    }
    
    public void rejectInvitation() {
        if (selectedNotification != null) {
            ejb.rejectInvitation(selectedNotification);
        }
        RequestContext.getCurrentInstance().closeDialog(null);
        notifications.remove(selectedNotification);
        selectedNotification = null;
        RequestContext.getCurrentInstance().update(":notifPanell");
    }
    
    public void deleteNotification() {
        if (selectedNotification != null) {
            ejb.removeEntity(selectedNotification.getNotifId(), Notification.class);
        }
        RequestContext.getCurrentInstance().closeDialog(null);
        notifications.remove(selectedNotification);
        selectedNotification = null;
        RequestContext.getCurrentInstance().update(":notifPanell");
    }

    public Notification getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification = selectedNotification;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
