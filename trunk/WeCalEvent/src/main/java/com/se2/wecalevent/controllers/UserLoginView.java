/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class UserLoginView {

    @EJB
    private sessionBeanRemote ejb;

    private String email;
    private String password;
    private User theUser;
    private boolean loggedIn;
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

    public User getTheUser() {
        return theUser;
    }

    public void setTheUser(User theUser) {
        this.theUser = theUser;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String login() {
        FacesMessage message = null;
        theUser = ejb.loginUser(email, password);
        password = null;
        loggedIn = theUser != null;
        if (loggedIn) {
            message = new FacesMessage("Hello", theUser.getName());
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "home";
        }
        else {
            message = new FacesMessage("Ciao","I'm sorry, can't let you in :(");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index";
        }
        
    }
    
    public String logout() {
        FacesMessage message = new FacesMessage("Byebye", theUser.getName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        theUser = null;
        email = null;
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }
    
    public void controlLogin(boolean status) throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("hello");
        if (status && !loggedIn)
            context.redirect("index.xhtml");
        if (!status && loggedIn)
            context.redirect("home.xhtml");
    }
    
    
}
