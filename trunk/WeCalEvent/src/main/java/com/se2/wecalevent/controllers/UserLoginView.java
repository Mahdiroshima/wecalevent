/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;


@ManagedBean
@SessionScoped
public class UserLoginView {

    @EJB
    private sessionBeanRemote ejb;
    private String txt1;
    private String email;
    private String password;
    private User theUser;
    private boolean loggedIn;
    private List<User> allPeople;
    private List<User> people;
    private List<String> selectedPeople;
    private Properties acDict = new Properties();
    
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

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
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
    //initializes allPeople first
    @PostConstruct
    public void init() {
        allPeople = ejb.getAllUsers();
    }
    
    /**
     * action method for login operation
     * @return 
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = null;
        theUser = ejb.loginUser(email, password);
        password = null;
        loggedIn = theUser != null;
        //redirect to home page if the creditiantials are correct
        if (loggedIn) {
            return "home.xhtml?faces-redirect=true";
        }
        else { //show a message if login fails
            message = new FacesMessage("Ciao","I'm sorry, can't let you in :(");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "index.xhtml?faces-redirect=true";
        }
        
    }
    /**
     * Action method for logout operation
     * it destroys the current session
     * @return 
     */
    public String logout() {
        FacesMessage message = new FacesMessage("Byebye", theUser.getName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        theUser = null;
        email = null;
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }
    public void invalidate() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    /**
     * this is generic page access control to page for unauthorized views
     * @param status
     * @param link
     * @throws IOException 
     */
    public void controlLogin(boolean status, String link) throws IOException {
        //if a page is only for not logged in users send false and the redirected link
        //if a page is only for logged in users send true and the redirected link
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (status && !loggedIn)
            context.redirect(link);
        if (!status && loggedIn)
            context.redirect(link);
    }
    /**
     * Action method for profile editting it redirects to update profile page
     * @return 
     */
    public String editProfile() {
        return "updateProfile.xhtml?id="+ theUser.getUserId() + "&faces-redirect=true";
    }
    /**
     * It returns list of names for auto complate field
     * @param query
     * @return 
     */
    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        acDict = new Properties();
        for (User user: allPeople) {
            String fullName = user.getName() + " " + user.getSurname();
            fullName = fullName.toLowerCase();
            if (fullName.contains(query.toLowerCase())) {
                results.add(user.getName() + " " + user.getSurname());
                acDict.put(user.getName() + " " + user.getSurname(),user.getUserId());
            }
        }

        return results;
    }
    /**
     * On item select of autocomplete view
     * it redirects to the calendar of requested user
     * @param event 
     */
    public void onItemSelect(SelectEvent event) {
        String key = event.getObject().toString();
        if (key != null) {
            Integer value = (Integer) acDict.get(key);
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml?id=" + value);
            } catch (IOException ex) {
                Logger.getLogger(UserLoginView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
