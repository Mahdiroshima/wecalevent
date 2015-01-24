/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mert
 */
@ManagedBean
@ViewScoped
public class UserRegisterView {

    @EJB
    private sessionBeanRemote ejb;

    /**
     * Creates a new instance of UserRegisterView
     */
    public UserRegisterView() {
    }
    @ManagedProperty("#{userLoginView}")
    private UserLoginView userLoginView;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String passwordAgain;
    private String calendarVisibility;
    private String userID;
    
    public UserLoginView getUserLoginView() {
        return userLoginView;
    }

    public void setUserLoginView(UserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        init(); 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getCalendarVisibility() {
        return calendarVisibility;
    }

    public void setCalendarVisibility(String calendarVisibility) {
        this.calendarVisibility = calendarVisibility;
    }

    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        boolean status = ejb.register(email, password, calendarVisibility, name, surname);
        FacesMessage message = null;
        if (status) {
            message = new FacesMessage("Bravo", "You are now registered");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index.xhtml?faces-redirect=true";
        } else {
            message = new FacesMessage("Sorry", "This e-mail has been taken by another user");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "register.xhtml?faces-redirect=true";
        }
    }

    public String updateprofile() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        int uid = Integer.parseInt(userID);
        boolean status = ejb.updateUser(uid, email, password, calendarVisibility, name, surname);
        FacesMessage message = null;
        if (status) {
            message = new FacesMessage("Your profile is updated");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "home.xhtml?faces-redirect=true";
        } else {
            message = new FacesMessage("Sorry", "this email is already taken");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "updateProfile.xhtml?id=" + uid + "&faces-redirect=true";
        }
    }

    @PostConstruct
    public void init() {
        User user = ejb.getUser(); 
        if (user != null && userLoginView.isLoggedIn() == true) {
            this.email = user.getEmail();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.password = user.getPass();
            this.calendarVisibility = user.getCalendar();
        }
    }

}
