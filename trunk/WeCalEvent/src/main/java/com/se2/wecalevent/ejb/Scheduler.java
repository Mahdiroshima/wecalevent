/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.ejb;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.Weather;
import com.se2.wecalevent.remote.sessionBeanRemote;
import com.se2.wecalevent.util.WeatherAPI;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class Scheduler {

    @PersistenceContext(unitName = "com.se2_WeCalEvent_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;

    private final Logger log = Logger
            .getLogger(Scheduler.class.getName());

    @EJB
    private sessionBeanRemote ejb;
//This method will check weather forecast each 12 hours and notify participant about any changes. 
    @Schedule(minute = "0", hour = "1,13")
    public void runEvery12hours() {
        log.log(Level.INFO,
                "running every minute .. now it's: " + new Date().toString());
        //Excute query to get all current event 
        Query query = entityManager.createNamedQuery("Event.findAll");
        //Create list of events
        List<Event> res = query.getResultList();
        //for each event
        for (Event e : res) {
            // Get event by ID
            Event theEvent = entityManager.find(Event.class, e.getEventId());
            //Get the starting date of the event
            Date startingTime = e.getStartingDate();
            //Get the Location of the event 
            String city = e.getLocationCity();
            //Call the Weather Forecast for the City and date 
            Weather weather = null;
            try {
                //Get weather forecast of the event 
                weather = WeatherAPI.getWeatherForecast(startingTime, city);
            } catch (NullPointerException exception) {
                //initialize the event to the previous forecast
                //this does not happen, it is written for preserving 
                //the state of the schedule
                weather = theEvent.getWeatherId();
            }
            //Get the stored weather condition
            String storedweathercond = weather.getWeatherCondition();
            //Get the current weather condition
            String currentlyweathercond = theEvent.getWeatherId().getWeatherCondition();
            //Compare the stored and the current weather condition 
            //if differenet upadate the weather object
            if (storedweathercond.compareTo(currentlyweathercond) != 0) {
                //Send a notification:
                String notice = "The weather forecast for the event: " + theEvent.getEventName()
                        + " has changed to " + storedweathercond + " from " + currentlyweathercond;
                theEvent.getUserList().size();
                //Call NotifyParticipant method
                ejb.notifyParticipant(theEvent, theEvent.getUserList(), notice);
                //Get the WeatherID related to the event
                theEvent.getWeatherId(); 
                //Get the old weather condition
                theEvent.getWeatherId().getWeatherCondition();
                //Set the new weather condition
                theEvent.getWeatherId().setWeatherCondition(storedweathercond);
                //Set updates
                entityManager.merge(theEvent);
                entityManager.flush();
            }

        }
    }
}
