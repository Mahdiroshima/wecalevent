package com.se2.wecalevent.entities;

import com.se2.wecalevent.entities.Event;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-25T16:42:37")
@StaticMetamodel(Weather.class)
public class Weather_ { 

    public static volatile SingularAttribute<Weather, Integer> weatherId;
    public static volatile ListAttribute<Weather, Event> eventList;
    public static volatile SingularAttribute<Weather, Date> weatherDate;
    public static volatile SingularAttribute<Weather, String> city;
    public static volatile SingularAttribute<Weather, String> weatherCondition;

}