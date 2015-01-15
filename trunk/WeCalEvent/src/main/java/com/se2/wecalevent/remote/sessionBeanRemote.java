/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.remote;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Mert
 */


@Remote
public interface sessionBeanRemote {
    public User getUser();
    public User loginUser(String email, String password);
    public boolean register(String email, String password, String calendar, String name, String surname);
    public boolean createEvent(String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate, List<User> invitedList);
    public boolean inviteUsers(Event event, List<User> users);
    public boolean notifyInvitation(Event event, List<User> users);
    public boolean notifyParticipant(Event event, List<User> users, String notice);
    public boolean notifyOwner(Event event, User user, String notice);
    public List<Event> getEventsOfUser(int user_id);
    public User getUserById(int user_id);
    public List<User> getAllUsers();
    public boolean updateEvent (Integer eventId,String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate);
    public boolean updateUser(Integer userId, String email, String password, String calendar, String name, String surname);
    public boolean removeEntity (Integer objectId, Class t);
    public boolean removeEntity (Object object);
}
