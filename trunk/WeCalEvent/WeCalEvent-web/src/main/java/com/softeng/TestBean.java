/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softeng;


import Entities.Person;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mert
 */

@ManagedBean
@SessionScoped
public class TestBean {
    com.softeng.NewSessionBean newSessionBean;
    private Person person;

    @EJB
    private NewSessionBean personService;

    @PostConstruct
    public void init() {
        person = new Person();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void save() {
        personService.create(person);
        
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Person successfully created, new ID is " + person.getId()));
        person = new Person();
    }
/*
    private com.softeng.NewSessionBean lookupNewSessionBeanBean() {
        try {
            Context c = new InitialContext();
            return (com.softeng.NewSessionBean) c.lookup("java:global/com.softeng_WeCalEvent-ear_ear_1.0-SNAPSHOT/com.softeng_WeCalEvent-ejb_ejb_1.0-SNAPSHOT/NewSessionBean!com.softeng.NewSessionBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }*/
}
