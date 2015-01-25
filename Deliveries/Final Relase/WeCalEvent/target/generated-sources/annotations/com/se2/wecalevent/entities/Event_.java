package com.se2.wecalevent.entities;

import com.se2.wecalevent.entities.Notification;
import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.entities.Weather;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T16:42:37")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, Integer> eventId;
    public static volatile ListAttribute<Event, Notification> notificationList;
    public static volatile SingularAttribute<Event, String> visibility;
    public static volatile ListAttribute<Event, User> userList1;
    public static volatile SingularAttribute<Event, String> desiredWeather;
    public static volatile SingularAttribute<Event, User> creatorId;
    public static volatile SingularAttribute<Event, String> eventType;
    public static volatile SingularAttribute<Event, Weather> weatherId;
    public static volatile SingularAttribute<Event, Date> endingDate;
    public static volatile ListAttribute<Event, User> userList;
    public static volatile SingularAttribute<Event, String> eventDescription;
    public static volatile SingularAttribute<Event, String> eventName;
    public static volatile SingularAttribute<Event, Date> startingDate;
    public static volatile SingularAttribute<Event, String> locationCity;

}