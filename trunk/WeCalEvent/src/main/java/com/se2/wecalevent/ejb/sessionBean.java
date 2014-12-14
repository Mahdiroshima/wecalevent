/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.ejb;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.entities.Weather;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Mert
 */
@Stateless
public class sessionBean implements sessionBeanRemote {

    @PersistenceContext(unitName = "com.se2_WeCalEvent_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;
    private User user;

    @Override
    public User loginUser(String email, String password) {
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User loggedin = (User) query.getSingleResult();
        if (loggedin.getPass().equals(password)) {
            user = loggedin;
            return loggedin;
        }
        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public boolean register(String email, String password, String calendar, String name, String surname) {
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User userExists = null;
        try {
            userExists = (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
        if (userExists != null) {
            return false;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setPass(password);
        newUser.setCalendar(calendar);
        entityManager.persist(newUser);
        return true;
    }

    @Override
    public boolean createevent(String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate) {
        Query query = entityManager.createNamedQuery("Event.findOverlap");
        query.setParameter("dateStarting", startingDate);
        query.setParameter("dateEnding", endingDate);
        Event samedate = null;
        try {
            samedate = (Event) query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            
        }
        
        query = entityManager.createNamedQuery("Weather.findAll");
        Weather w = (Weather) query.getSingleResult();
        Event NewEvent = new Event();
        NewEvent.setCreatorId(user);
        NewEvent.setEventName(eventName);
        NewEvent.setEventDescription(eventDescription);
        NewEvent.setEventType(eventType);
        NewEvent.setDesiredWeather(desiredWeather);
        NewEvent.setVisibility(visibility);
        NewEvent.setLocationCity(locationCity);
        NewEvent.setStartingDate(startingDate);
        NewEvent.setEndingDate(endingDate);
        NewEvent.setWeatherId(w);
        entityManager.persist(NewEvent);
        return true;

    }

    @Override
    public User getUser() {
        return user;
    }
}
