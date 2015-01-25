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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Mert
 */
@Startup
@Singleton
public class TimerInserter {

    @EJB
    private sessionBeanRemote ejb;

    @Resource
    private TimerService timerService;

    @PersistenceContext(unitName = "com.se2_WeCalEvent_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;

    private final Logger log = Logger.getLogger(getClass().getName());

    private Date getClosestPossibleDate(Event event) {
        long duration = event.getEndingDate().getTime() - event.getStartingDate().getTime();
        String[] desiredList = event.getDesiredWeather().split("-");
        Date closestDate = (Date) event.getStartingDate().clone();
        int hour = closestDate.getHours();
        //16  +2 15 +3 17 +1
        closestDate.setHours(hour + 3 - hour % 3);
        closestDate.setMinutes(0);
        closestDate.setSeconds(0);

        Date limit = new Date();
        limit.setTime(limit.getTime() + 15 * 24 * 60 * 60 * 1000);
        Date limitDaily = new Date();
        limitDaily.setTime(limitDaily.getTime() + 4 * 24 * 60 * 60 * 1000);
        do {
            //execute
            Weather weather = WeatherAPI.getWeatherForecast(closestDate, event.getLocationCity());
            //if weather is what I want
            boolean flag = false;
            for (String desired : desiredList) {
                if (desired != null && desired.length() > 1) {
                    if (desired.equals(weather.getWeatherCondition())) {
                        flag = true;
                        break;
                    }
                }
            }
            //You found your closest day well congrats
            if (flag) {
                if (this.checkExistingEvent(closestDate, duration)) {
                    return closestDate;
                }
            }
            //increment
            if (closestDate.compareTo(limitDaily) < 0) {
                //increment 3 hour
                closestDate.setTime(closestDate.getTime() + 1000 * 60 * 60 * 3);
            } else {
                //increment 1 day
                closestDate.setTime(closestDate.getTime() + 1000 * 60 * 60 * 24);
                closestDate.setHours(0);
            }
        } while (closestDate.compareTo(limit) < 0);
        return null;
    }

    /**
     *
     * @return
     */
    private void executeOwnerNotification(Event event) {
        if (event == null || event.getEventId() == null) {
            return;
        }
        User owner = event.getCreatorId();
        Event managedEvent = entityManager.find(Event.class, event.getEventId());
        Date startingTime = managedEvent.getStartingDate();
        //Get the Location of the event 
        String city = managedEvent.getLocationCity();
        //Call the Weather Forecast for the City and date 
        Weather weather = null;
        try {
            weather = WeatherAPI.getWeatherForecast(startingTime, city);
        } catch (NullPointerException exception) {
            weather = managedEvent.getWeatherId();
        }
        //Get the stored weather condition
        String currentWeather = weather.getWeatherCondition();
        //Get the desired weather
        String desiredWeather = (String) managedEvent.getDesiredWeather();
        managedEvent.getWeatherId();
        managedEvent.getWeatherId().getWeatherCondition();
        managedEvent.getWeatherId().setWeatherCondition(currentWeather);
        if (managedEvent.getEventType().contains("outdoor")) {
            String[] desiredList = desiredWeather.split("-");
            boolean flag = false;
            for (String desired : desiredList) {
                if (desired != null && desired.length() > 1) {
                    if (desired.equals(currentWeather)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                //there is a bad weather condition suggest a new date
                Date closestDate = this.getClosestPossibleDate(managedEvent);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateString = df.format(closestDate);
                String notice = "The weather forecast for your event: " + managedEvent.getEventName() + " is "
                        + currentWeather + " which is not in your desired list. We suggest you to change your starting date to " + dateString;
                ejb.notifyOwner(managedEvent, owner, notice);
            }
        }
        entityManager.merge(managedEvent);
        entityManager.flush();
    }

    /**
     * This method creates notifications for participant users if the weather
     * forecast is updated
     *
     * @return
     */
    private void executeParticipateNotification(Event event) {
        if (event == null || event.getEventId() == null) {
            return;
        }
        Event managedEvent = entityManager.find(Event.class, event.getEventId());
        Date startingTime = managedEvent.getStartingDate();
        //Get the Location of the event 
        String city = managedEvent.getLocationCity();
        //Call the Weather Forecast for the City and date 
        Weather weather = null;
        try {
            weather = WeatherAPI.getWeatherForecast(startingTime, city);
        } catch (NullPointerException exception) {
            weather = managedEvent.getWeatherId();
        }
        //Get the stored weather condition
        String currentWeather = weather.getWeatherCondition();
        //Get the desired weather
        String desiredWeather = (String) managedEvent.getDesiredWeather();
        managedEvent.getWeatherId();
        managedEvent.getWeatherId().getWeatherCondition();
        managedEvent.getWeatherId().setWeatherCondition(currentWeather);
        if (managedEvent.getEventType().contains("outdoor")) {
            String[] desiredList = desiredWeather.split("-");
            boolean flag = false;
            for (String desired : desiredList) {
                if (desired != null && desired.length() > 1) {
                    if (desired.equalsIgnoreCase(currentWeather)) {
                        flag = true;
                        break;
                    }
                }
            }
            //Compare the stored and the current weather condition if differenet upadate the weather object
            if (!flag) {
                //Send a notification:
                String notice = "The weather forecast for the event: " + managedEvent.getEventName()
                        + " is " + currentWeather + " and it is not in your desired weather list";
                managedEvent.getUserList().size();
                ejb.notifyParticipant(managedEvent, managedEvent.getUserList(), notice);
            }
        }
        entityManager.merge(managedEvent);
        entityManager.flush();
    }

    @PostConstruct
    public void createProgrammaticalTimer() {
        log.log(Level.INFO, "ProgrammaticalTimerEJB initialized");
        Query query = entityManager.createNamedQuery("Event.findAll");
        //Create list of events
        List<Event> res = query.getResultList();
        //for each event
        for (Event event : res) {
            createTimer(event);
        }

    }

    @Timeout
    public void handleTimer(final Timer timer) {
        log.info("timer received");
        if (timer.getInfo() instanceof Event) {

            Event event = (Event) timer.getInfo();
            if (!checkEventTimeIsUpToDate(event)) {
                timer.cancel();
                return;
            }
            log.info("timer received - contained message is: " + event.getEventName());
            Date startingDate = event.getStartingDate();
            long differenceInMS = startingDate.getTime() - new Date().getTime();
            if (differenceInMS > 0 && differenceInMS < 1000 * 60 * 60 * 48) {
                this.executeParticipateNotification(event);
            } else if (differenceInMS > 0 && differenceInMS < 1000 * 60 * 60 * 96) {
                this.executeOwnerNotification(event);
            }
            timer.cancel();
        }
    }

    private boolean checkExistingEvent(Date closestDate, long duration) {
        Date endingDate = (Date) closestDate.clone();
        endingDate.setTime(endingDate.getTime() + duration);
        //Creates an predefined SQL Query, which checks if there is an event which will occur in the given interval
        Query query = entityManager.createNamedQuery("Event.findOverlap");
        query.setParameter("dateStarting", closestDate);
        query.setParameter("dateEnding", endingDate);
        List<Event> samedate = null;
        try {
            //if NoResultException is checked, no problem
            samedate = query.getResultList();
            //There is an event in the given interval, return false
            if (samedate.size() > 0) {
                return false;
            }
        } catch (NoResultException e) {

        } catch (NonUniqueResultException e) {

        }
        return true;
    }

    public void createTimer(Event event) {
        Date theDate = (Date) event.getStartingDate().clone();
        theDate.setTime(theDate.getTime() - 1000 * 60 * 60 * 24);
//            Testing purposes
//            Date theDate = new Date();
//            theDate.setTime(theDate.getTime() + 1000*60);
        if (new Date().before(theDate)) {
            // it is okey to executeParticipateNotification
            timerService.createCalendarTimer(createScheduleExpression(theDate),
                    new TimerConfig(event, false));
            log.info("timer created for " + theDate.toString() + " the event: " + event.getEventName());
        }
        theDate.setTime(theDate.getTime() - 1000 * 60 * 60 * 48);
        if (new Date().before(theDate)) {
            // it is okey to executeParticipateNotification
            timerService.createCalendarTimer(createScheduleExpression(theDate),
                    new TimerConfig(event, false));
            log.info("timer created for " + theDate.toString() + " the event: " + event.getEventName());
        }
    }
    /**
     * This method checks wheter the timer is still valid or not
     * @param event
     * @return 
     */
    private boolean checkEventTimeIsUpToDate(Event event) {
        Date theDate = (Date) event.getStartingDate().clone();
        theDate.setTime(theDate.getTime() - 1000 * 60 * 60 * 24);
        long delta = 1000 * 120;//2 minutes
        Date x1 = new Date();
        x1.setTime(x1.getTime() - delta);
        Date x2 = new Date();
        x2.setTime(x2.getTime() + delta);
        boolean case1 = theDate.getTime() > x1.getTime()
                && theDate.getTime() < x2.getTime();
        theDate.setTime(theDate.getTime() - 1000 * 60 * 60 * 48);
        boolean case2 = theDate.getTime() > x1.getTime()
                && theDate.getTime() < x2.getTime();
        return case1 || case2;
    }
    
    private ScheduleExpression createScheduleExpression(Date date) {
        ScheduleExpression s = new ScheduleExpression();
        s.hour(date.getHours());
        s.minute(date.getMinutes());
        s.second(date.getSeconds());
        s.dayOfMonth(date.getDate());
        s.month(date.getMonth() + 1);
        s.year(date.getYear() + 1900);
        return s;
    }
}
