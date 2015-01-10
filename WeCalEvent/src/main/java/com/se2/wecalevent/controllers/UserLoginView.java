/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.io.IOException;
import java.util.List;
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
    private List<User> people;
    private List<String> selectedPeople;
    
    public List<User> getPeople() {
        return people;
    }

    public void setPeople(List<User> people) {
        this.people = people;
    }

    public List<String> getSelectedPeople() {
        return selectedPeople;
    }

    public void setSelectedPeople(List<String> selectedPeople) {
        this.selectedPeople = selectedPeople;
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
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = null;
        theUser = ejb.loginUser(email, password);
        password = null;
        loggedIn = theUser != null;
        if (loggedIn) {
            message = new FacesMessage("Hello", theUser.getName());
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            return "home.xhtml?faces-redirect=true";
        }
        else {
            message = new FacesMessage("Ciao","I'm sorry, can't let you in :(");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index.xhtml?faces-redirect=true";
        }
        
    }
    
    public String logout() {
        FacesMessage message = new FacesMessage("Byebye", theUser.getName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        theUser = null;
        email = null;
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }
    
    public void controlLogin(boolean status, String link) throws IOException {
        //if a page is only for not logged in users send false and the redirected link
        //if a page is only for logged in users send true and the redirected link
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (status && !loggedIn)
            context.redirect(link);
        if (!status && loggedIn)
            context.redirect(link);
    }
    
    
}
