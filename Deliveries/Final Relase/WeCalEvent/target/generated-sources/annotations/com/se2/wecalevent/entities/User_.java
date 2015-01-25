package com.se2.wecalevent.entities;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.Notification;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T16:42:37")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> calendar;
    public static volatile ListAttribute<User, Event> eventList2;
    public static volatile ListAttribute<User, Event> eventList1;
    public static volatile ListAttribute<User, Event> eventList;
    public static volatile ListAttribute<User, Notification> notificationList;
    public static volatile SingularAttribute<User, String> pass;
    public static volatile SingularAttribute<User, String> surname;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Integer> userId;
    public static volatile SingularAttribute<User, String> email;

}