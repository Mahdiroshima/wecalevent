    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.ejb;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.Notification;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.entities.Weather;
import com.se2.wecalevent.remote.sessionBeanRemote;
import com.se2.wecalevent.util.DateException;
import com.se2.wecalevent.util.EmailAPI;
import com.se2.wecalevent.util.HelperMethods;
import com.se2.wecalevent.util.WeatherAPI;
import com.se2.wecalevent.viewModels.NotificationViewModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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

    @EJB
    private TimerInserter timerInserter;

    @Override
    public User loginUser(String email, String password) {
        // Create a predefined query to check the e-mail exists or not
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User loggedin = null;
        try {
            loggedin = (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
        //If the query return a user and the Passwor is correct 
        //user will be logged in
        if (loggedin != null && loggedin.getPass().equals(password)) {
            user = entityManager.find(User.class, loggedin.getUserId());
            return user;
        }
        return null;
    }

    /**
     * This ejb method creates an user in the database with the given parameters
     *
     * @param email Unique e-mail of the new User for login, mandatory
     * @param password Plain password of the user, mandatory
     * @param calendar Calendar privacy of the user, public or private,
     * mandatory
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
        newUser.setName(HelperMethods.prettifyName(name));
        newUser.setSurname(HelperMethods.prettifyName(surname));
        newUser.setPass(password);
        newUser.setCalendar(calendar);
        //Save the user to the database
        entityManager.persist(newUser);
        return true;
    }

    /**
     * This ejb method creates an event with the given parameters
     *
     * @param eventName Name of the event, mandatory
     * @param eventDescription Description of the event, mandatory
     * @param eventType Type of the event, outdoor or indoor, mandatory
     * @param desiredWeather List of desired weather conditions as a string, ex:
     * cloudy-sunny, mandatory
     * @param visibility Privacy of the event, public or private, mandatory
     * @param locationCity The place in which the event will be hosted,
     * mandatory, required for forecast gathering
     * @param startingDate Starting date of the event
     * @param endingDate Ending date of the event
     * @param invitedList The list of users who are invited
     * @return true if create operation is successful, false otherwise
     */
    @Override
    public boolean createEvent(String eventName, String eventDescription, String eventType, String desiredWeather,
            String visibility, String locationCity, Date startingDate, Date endingDate, List<User> invitedList) throws DateException {
        //Check if the starting date of the event is in the past
        if (startingDate.before(new Date())) {
            throw new DateException("Your starting date cannot be in the past");
        }
        //Check if the event ends before its starts
        if (startingDate.after(endingDate)) {
            throw new DateException("Your starting date is after the ending date");
        }
        //Creates an predefined SQL Query, which checks if there is an event which will occur in the given interval
        Query query = entityManager.createNamedQuery("Event.findOverlap");
        query.setParameter("dateStarting", startingDate);
        query.setParameter("dateEnding", endingDate);
        List<Event> samedate = null;
        try {
            //if NoResultException is checked, no problem
            samedate = query.getResultList();
            //There is an event in the given interval, return false
            if (samedate.size() > 0) {
                throw new DateException("There is another event between those days");
            }
        } catch (NoResultException e) {

        } catch (NonUniqueResultException e) {

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
        entityManager.flush();
        //Save the Ivited people
        inviteUsers(NewEvent, invitedList);        
        entityManager.merge(NewEvent);
        entityManager.flush();
        //Create timer for the new event for Notification about forecast
        timerInserter.createTimer(NewEvent);
        return true;
    }

    /**
     * This method returns the list of events requested for the user
     *
     * @param user_id
     * @return
     */
    @Override
    public List<Event> getEventsOfUser(int user_id) {
        if (user == null) {
            return null;
        }
        entityManager.flush();
        User theUser = entityManager.find(User.class, user_id);
        entityManager.refresh(theUser);
        entityManager.merge(user);
        theUser.getEventList().size();
        return theUser.getEventList();
    }

    /**
     * This method is to look for user by his ID 
     * 
     * @param user_id
     * @return  user
     */
    @Override
    public User getUserById(int user_id) {
        Query query = entityManager.createNamedQuery("User.findByUserId");
        query.setParameter("userId", user_id);
        User theUser = null;
        try {
            theUser = (User) query.getSingleResult();
        } catch (NoResultException e) {

        }
        theUser.getEventList().size();
        theUser.getEventList1().size();
        theUser.getEventList2().size();
        return theUser;
    }

    /**
     * Get method for the User object, this method can be used to check whether
     * the user is logged in or not
     *
     * @return
     */
    @Override
    public User getUser() {
        if (user != null) {
            user = entityManager.find(User.class, user.getUserId());
        }
        return user;
    }

    /**
     * TODO: update javadoc
     *
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
    public boolean updateEvent(Integer eventId, String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate, List<User> invitedUsers) throws DateException {
        //Check if the the satrting date of the event is not in  past 
        if (startingDate.before(new Date())) {
            throw new DateException("Your starting date cannot be in the past");
        }
        //check if the ending date of the event is not in before the starting date
        if (startingDate.after(endingDate)) {
            throw new DateException("Your starting date is after the ending date");
        }
        Event event = entityManager.find(Event.class, eventId);
        //Creates an predefined SQL Query, which checks if there is an event which will occur in the given interval
        Query query = entityManager.createNamedQuery("Event.findOverlap");
        query.setParameter("dateStarting", startingDate);
        query.setParameter("dateEnding", endingDate);
        List<Event> samedate = null;
        try {
            //if NoResultException is checked, no problem
            samedate = query.getResultList();
            //There is an event in the given interval, return false
            if (samedate.size() == 1 && samedate.get(0).getEventId().equals(eventId)) {
                //no problem
            } else if (samedate.size() > 0) {
                throw new DateException("There is another event between those days");
            }
        } catch (NoResultException e) {

        } catch (NonUniqueResultException e) {

        }
        //Update event informations
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setEventType(eventType);
        event.setDesiredWeather(desiredWeather);
        event.setVisibility(visibility);
        event.setLocationCity(locationCity);
        event.setStartingDate(startingDate);
        event.setEndingDate(endingDate);
        entityManager.merge(event);
        entityManager.flush();
        //Update the list of invited users
        inviteUsers(event, invitedUsers);
        //Create timer for the updated event
        timerInserter.createTimer(event);
        return true;

    }

    /**
     * TODO: update javadoc
     *
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
        // Create a predefined query to check the e-mail exists or not
        User user = entityManager.find(User.class, userId);
        if (!user.getEmail().equals(email)) {
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
        }
        //Update the user informations
        user.setEmail(email);
        user.setPass(password);
        user.setCalendar(calendar);
        user.setSurname(HelperMethods.prettifyName(surname));
        user.setName(HelperMethods.prettifyName(name));
        entityManager.merge(user);
        entityManager.flush();
        return true;

    }
/**
 * Get all the users
 * @return list of users
 */
    @Override
    public List<User> getAllUsers() {
        Query query = entityManager.createNamedQuery("User.findAll");
        //Create list of users
        List<User> result = query.getResultList();
        return result;
    }

    @Override
    public boolean inviteUsers(Event event, List<User> users) {
        if (users != null && event != null) {
            //Set the list of invited users
            if (event.getUserList1() == null) {
                event.setUserList1(users);
            } else {
                event.getUserList1().addAll(users);
            }
            entityManager.merge(event);
            entityManager.flush();
            //Send notification to all invited list 
            notifyInvitation(event, users);
            return true;
        }
        return false;
    }

    @Override
    public boolean notifyInvitation(Event event, List<User> users) {
        if (event != null && users != null) {
            entityManager.refresh(event);
            for (User theUser : users) {
                //Set notification information in the database 
                Notification notification = new Notification();
                notification.setNotifType(NotificationViewModel.NotificationType.Invitation.getValue());
                notification.setUserId(theUser);
                notification.setNotification("You are invited to the event: " + event.getEventName());
                notification.setRelatedTo(event);
                notification.setTs(new Date());
                //Save in the data base
                entityManager.persist(notification);
                //Send an email to the invited users
                String content = "You're invited to this event: " + event.getEventName()
                        + " which will start on: " + event.getStartingDate().toString();
                EmailAPI.sendEmail(theUser.getEmail(), "Your are invited, " + theUser.getName() + "!", content);
            }
            entityManager.flush();
            return true;
        }
        return false;
    }

    @Override
    public boolean notifyParticipant(Event event, List<User> users, String notice) {
        if (event != null && users != null) {
            for (User theUser : users) {
                //Set notification informayion 
                Notification notification = new Notification();
                notification.setNotifType(NotificationViewModel.NotificationType.Forecast_change.getValue());
                notification.setUserId(theUser);
                notification.setNotification(notice);
                notification.setRelatedTo(event);
                notification.setTs(new Date());
                //save in database 
                entityManager.persist(notification);
                //send an email with notifcation about a forecast related to an event
                EmailAPI.sendEmail(theUser.getEmail(), "New notification about: " + event.getEventName(), notice);
            }
            entityManager.flush();
            return true;
        }
        return false;
    }

    @Override
    public boolean notifyOwner(Event event, User theUser, String notice) {
        if (event != null && theUser != null) {
            //Set notification information 
            Notification notification = new Notification();
            notification.setNotifType(NotificationViewModel.NotificationType.Postpone_suggestion.getValue());
            notification.setUserId(theUser);
            notification.setNotification(notice);
            notification.setRelatedTo(event);
            notification.setTs(new Date());
            //Save information in database
            entityManager.persist(notification);
            entityManager.flush();
            //Notifiy the owner by email
            EmailAPI.sendEmail(theUser.getEmail(), "New notification about: " + event.getEventName(), notice);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEntity(Integer objectId, Class t) {
        //Generic method to delete entity from database
        if (objectId != null && t != null) {
            Object object = entityManager.find(t, objectId);
            entityManager.remove(object);
            entityManager.flush();
            return true;
        }
        return false;
    }
    /**
     * Method to get user notificatio list
     * @return 
     */
    @Override
    public List<Notification> getNotifications() {
        
        if (user != null) {
            entityManager.merge(user);
            user.getNotificationList().size();
            return user.getNotificationList();
        }
        return null;
    }
/**
 * Methode related to user accept to participate to an event
 * @param notification
 * @return 
 */
    @Override
    public boolean acceptInvitation(Notification notification) {
        if (user != null) {
            Event event = entityManager.find(Event.class, notification.getRelatedTo().getEventId());
            event.getUserList().size();
            event.getUserList().add(user);
            notification = entityManager.find(Notification.class, notification.getNotifId());
            entityManager.remove(notification);
            entityManager.merge(event);
            entityManager.flush();
            return true;
        }
        return false;
    }
/**
 * Methode related to user decline to partcipate to event
 * @param notification
 * @return 
 */
    @Override
    public boolean rejectInvitation(Notification notification) {
        if (user != null) {
            notification = entityManager.find(Notification.class, notification.getNotifId());
            entityManager.remove(notification);
            entityManager.flush();
            return true;
        }
        return false;
    }
/**
 * Methode to get the list of participant to an event
 * @param event_id
 * @return 
 */
    @Override
    public List<User> getParticipantsOfEvent(int event_id) {
        Event event = entityManager.find(Event.class, event_id);
        entityManager.merge(event);
        event.getUserList().size();
        return event.getUserList();
    }
/**
 * Method to search for an event by Event ID
 * @param id
 * @return 
 */
    @Override
    public Event getEventById(Integer id) {
        if (id != null) {
            return entityManager.find(Event.class, id);
        }
        return null;
    }

    /**
     * Methode to logout from application
     * @return 
     */
    @Override
    public boolean logout() {
        this.user = null;
        return true;
    }
}
