/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.remote;

import com.se2.wecalevent.entities.User;
import java.util.Date;
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
    public boolean createevent (String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate);
}
