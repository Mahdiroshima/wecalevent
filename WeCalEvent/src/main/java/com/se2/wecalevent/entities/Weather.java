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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mert
 */
@Entity
@Table(name = "weather")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Weather.findAll", query = "SELECT w FROM Weather w"),
    @NamedQuery(name = "Weather.findByWeatherId", query = "SELECT w FROM Weather w WHERE w.weatherId = :weatherId"),
    @NamedQuery(name = "Weather.findByWeatherDate", query = "SELECT w FROM Weather w WHERE w.weatherDate = :weatherDate"),
    @NamedQuery(name = "Weather.findByCity", query = "SELECT w FROM Weather w WHERE w.city = :city"),
    @NamedQuery(name = "Weather.findByWeatherCondition", query = "SELECT w FROM Weather w WHERE w.weatherCondition = :weatherCondition")})
public class Weather implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "weatherId")
    private List<Event> eventList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "weather_id")
    private Integer weatherId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weather_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weatherDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "weather_condition")
    private String weatherCondition;

    public Weather() {
    }

    public Weather(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public Weather(Integer weatherId, Date weatherDate, String city, String weatherCondition) {
        this.weatherId = weatherId;
        this.weatherDate = weatherDate;
        this.city = city;
        this.weatherCondition = weatherCondition;
    }

    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public Date getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(Date weatherDate) {
        this.weatherDate = weatherDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weatherId != null ? weatherId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Weather)) {
            return false;
        }
        Weather other = (Weather) object;
        if ((this.weatherId == null && other.weatherId != null) || (this.weatherId != null && !this.weatherId.equals(other.weatherId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.se2.wecalevent.entities.Weather[ weatherId=" + weatherId + " ]";
    }

    @XmlTransient
    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
    
}
