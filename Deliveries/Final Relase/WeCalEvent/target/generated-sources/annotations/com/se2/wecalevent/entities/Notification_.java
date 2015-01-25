package com.se2.wecalevent.entities;

import com.se2.wecalevent.entities.Event;
import com.se2.wecalevent.entities.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T16:42:37")
@StaticMetamodel(Notification.class)
public class Notification_ { 

    public static volatile SingularAttribute<Notification, Integer> notifId;
    public static volatile SingularAttribute<Notification, String> notification;
    public static volatile SingularAttribute<Notification, Integer> notifType;
    public static volatile SingularAttribute<Notification, User> userId;
    public static volatile SingularAttribute<Notification, Event> relatedTo;
    public static volatile SingularAttribute<Notification, Date> ts;

}