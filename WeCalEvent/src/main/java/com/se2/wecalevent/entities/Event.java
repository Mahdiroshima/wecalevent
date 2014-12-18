/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Mert Ergun <mert.rgun@gmail.com>
 */
@Entity
@Table(name = "event")
@NamedQueries({
    @NamedQuery(name = "Event.findOverlap", query = "SELECT e FROM Event e where (:dateStarting < e.startingDate and e.startingDate < :dateEnding)"
            + " or (:dateStarting < e.endingDate and e.endingDate < :dateEnding) or (:dateStarting >= e.startingDate and :dateEnding <= e.endingDate)"),
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findByEventId", query = "SELECT e FROM Event e WHERE e.eventId = :eventId"),
    @NamedQuery(name = "Event.findByEventName", query = "SELECT e FROM Event e WHERE e.eventName = :eventName"),
    @NamedQuery(name = "Event.findByEventType", query = "SELECT e FROM Event e WHERE e.eventType = :eventType"),
    @NamedQuery(name = "Event.findByDesiredWeather", query = "SELECT e FROM Event e WHERE e.desiredWeather = :desiredWeather"),
    @NamedQuery(name = "Event.findByVisibility", query = "SELECT e FROM Event e WHERE e.visibility = :visibility"),
    @NamedQuery(name = "Event.findByLocationCity", query = "SELECT e FROM Event e WHERE e.locationCity = :locationCity"),
    @NamedQuery(name = "Event.findByStartingDate", query = "SELECT e FROM Event e WHERE e.startingDate = :startingDate"),
    @NamedQuery(name = "Event.findByEndingDate", query = "SELECT e FROM Event e WHERE e.endingDate = :endingDate")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Integer eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "event_name")
    private String eventName;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "event_description")
    private String eventDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "event_type")
    private String eventType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "desired_weather")
    private String desiredWeather;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "visibility")
    private String visibility;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "location_city")
    private String locationCity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "starting_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startingDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ending_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingDate;
    @JoinTable(name = "participate", joinColumns = {
        @JoinColumn(name = "event_id", referencedColumnName = "event_id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private List<User> userList;
    @JoinTable(name = "intived", joinColumns = {
        @JoinColumn(name = "event_id", referencedColumnName = "event_id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    @ManyToMany
    private List<User> userList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventId")
    private List<NotificationParticipate> notificationParticipateList;
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User creatorId;
    @JoinColumn(name = "weather_id", referencedColumnName = "weather_id")
    @ManyToOne(optional = false)
    private Weather weatherId;

    public Event() {
    }

    public Event(Integer eventId) {
        this.eventId = eventId;
    }

    public Event(Integer eventId, String eventName, String eventDescription, String eventType, String desiredWeather, String visibility, String locationCity, Date startingDate, Date endingDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventType = eventType;
        this.desiredWeather = desiredWeather;
        this.visibility = visibility;
        this.locationCity = locationCity;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDesiredWeather() {
        return desiredWeather;
    }

    public void setDesiredWeather(String desiredWeather) {
        this.desiredWeather = desiredWeather;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList1() {
        return userList1;
    }

    public void setUserList1(List<User> userList1) {
        this.userList1 = userList1;
    }

    public List<NotificationParticipate> getNotificationParticipateList() {
        return notificationParticipateList;
    }

    public void setNotificationParticipateList(List<NotificationParticipate> notificationParticipateList) {
        this.notificationParticipateList = notificationParticipateList;
    }

    public User getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(User creatorId) {
        this.creatorId = creatorId;
    }

    public Weather getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Weather weatherId) {
        this.weatherId = weatherId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.se2.wecalevent.entities.Event[ eventId=" + eventId + " ]";
    }
    
}
