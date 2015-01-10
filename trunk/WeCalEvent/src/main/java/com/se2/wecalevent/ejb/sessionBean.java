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
import com.se2.wecalevent.util.WeatherAPI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        User loggedin = null;
         try { 
            loggedin = (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
         if (loggedin != null && loggedin.getPass().equals(password)) {
            user = loggedin;
            return loggedin;
        }
        return null;
    }

    /**
     * This ejb method creates an user in the database with the given parameters
     * @param email Unique e-mail of the new User for login, mandatory
     * @param password Plain password of the user, mandatory
     * @param calendar Calendar privacy of the user, public or private, mandatory
     * @param name Name of the User, mandatory
     * @param surname Surname of the User, mandatory
     * @return true if create operation is successful, false otherwise
     */
    @Override
    public boolean register(String email, String password, String calendar, String name, String surname) {
        // Create a predefined query to check the e-mail exists or not
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User userExists = null;
        try {
            userExists = (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
        //if this variable is null then the e-mail address doesn't exists in the database
        if (userExists != null) {
            return false;
        }
        //Create new user with the given parameters
        User newUser = new User();
        newUser.setEmail(email);
        //Prettier name and surname
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setPass(password);
        newUser.setCalendar(calendar);
        //Save the user to the database
        entityManager.persist(newUser);
        return true;
    }
    /**
     * This ejb method creates an event with the given parameters
     * @param eventName Name of the event, mandatory
     * @param eventDescription Description of the event, mandatory
     * @param eventType Type of the event, outdoor or indoor, mandatory
     * @param desiredWeather List of desired weather conditions as a string, ex: cloudy-sunny, mandatory
     * @param visibility Privacy of the event, public or private, mandatory
     * @param locationCity The place in which the event will be hosted, mandatory, required for forecast gathering
     * @param startingDate Starting date of the event
     * @param endingDate Ending date of the event
     * @return true if create operation is successful, false otherwise
     */
    @Override
    public boolean createEvent(String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate) {
        //Creates an predefined SQL Query, which checks if there is an event which will occur in the given interval
        Query query = entityManager.createNamedQuery("Event.findOverlap");
        query.setParameter("dateStarting", startingDate);
        query.setParameter("dateEnding", endingDate);
        Event samedate = null;
        try {
            //if NoResultException is checked, no problem
            samedate = (Event) query.getSingleResult();
            //There is an event in the given interval, return false
            return false;
        } catch (NoResultException e) {
            
        }
        //Get weather forecast form the WeatherAPI class
        Weather theWeather = WeatherAPI.getWeatherForecast(startingDate, locationCity);
        //Save the weather information to the database
        entityManager.persist(theWeather);
        //Create new event with given paramaters
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
        NewEvent.setWeatherId(theWeather);
        NewEvent.setUserList(new ArrayList<User>());
        NewEvent.getUserList().add(user);
        //Save the event to the database
        entityManager.persist(NewEvent);
        return true;
    }
    /**
     * This method returns the list of events requested for the user
     * @param user_id
     * @return 
     */
    @Override
    public List<Event> getEventsOfUser(int user_id) {
        Query query = entityManager.createNamedQuery("User.findByUserId");
        query.setParameter("userId", user_id);
        User theUser = null;
        try {
            theUser = (User) query.getSingleResult();
            if (theUser != null && user.getCalendar().equalsIgnoreCase("public")) {
               theUser.getEventList().size();
               return theUser.getEventList();
            }
        } catch (NoResultException e) {

        }
        return null;
    }
    
    @Override
    public User getUserById(int user_id) {
        Query query = entityManager.createNamedQuery("User.findByUserId");
        query.setParameter("userId", user_id);
        User theUser = null;
        try {
            theUser = (User) query.getSingleResult();
        } catch (NoResultException e) {
            
        }
        return theUser;
    }
    
    /**
     * Get method for the User object, this method can be used to check 
     * whether the user is logged in or not
     * @return 
     */
    @Override
    public User getUser() {
        return user;
    }
    /**
     * TODO: update javadoc
     * @param eventId
     * @param eventName
     * @param eventDescription
     * @param eventType
     * @param desiredWeather
     * @param visibility
     * @param locationCity
     * @param startingDate
     * @param endingDate
     * @return 
     */
    @Override
    public boolean updateEvent(Integer eventId, String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate) {
        Event event = entityManager.find(Event.class, eventId);
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setEventType(eventType);
        event.setDesiredWeather(desiredWeather);
        event.setVisibility(visibility);
        event.setLocationCity(locationCity);
        event.setStartingDate(startingDate);
        event.setEndingDate(endingDate);
        entityManager.persist(event);  
        return true;
             
    
    }
    /**
     * TODO: update javadoc
     * @param userId
     * @param email
     * @param password
     * @param calendar
     * @param name
     * @param surname
     * @return 
     */
    @Override
    public boolean updateUser(Integer userId, String email, String password, String calendar, String name, String surname) {
        User user = entityManager.find(User.class, userId);
        user.setEmail(email);
        user.setPass(password);
        user.setSurname(surname);
        user.setName(name);
        entityManager.persist(user);
        return true;
           
    }
    
    public boolean updateForecast(Integer eventId, Integer weatherId) {
        //Excute query to get all current event 
        Query query = entityManager.createNamedQuery("Event.findAll");
        //Create list of events
        List<Event> res = query.getResultList();
        //for each event
        for (Event e : res) {
            //Get the starting date of the event
            Date stratingtime = e.getStartingDate();
            //Get the Location of the event 
            String city = e.getLocationCity();
            //Call the Weather Forecast for the City and date 
            Weather weather = WeatherAPI.getWeatherForecast(stratingtime, city);
            //Get the stored weather condition
            String storedweathercond = weather.getWeatherCondition();
            //Get the current weather condition
            String currentlyweathercond = e.getWeatherId().getWeatherCondition();
            //Compare the stored and the current weather condition if differenet upadate the weather object
            if (storedweathercond.compareTo(currentlyweathercond) > 0) {
                e.getWeatherId().setWeatherCondition(storedweathercond);
            }

        }
        return true;
    }
    
   
            
}
