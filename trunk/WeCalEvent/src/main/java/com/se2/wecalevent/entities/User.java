/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Mert
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByCalender", query = "SELECT u FROM User u WHERE u.calender = :calender"),
    @NamedQuery(name = "User.findByPass", query = "SELECT u FROM User u WHERE u.pass = :pass")})
public class User implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "calender")
    private String calender;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "surname")
    private String surname;
    @Size(max = 20)
    @Column(name = "pass")
    private String pass;
    @ManyToMany(mappedBy = "userList")
    private List<Event> eventList;
    @ManyToMany(mappedBy = "userList1")
    private List<Event> eventList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creatorId")
    private List<Event> eventList2;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String email, String name, String surname, String calender) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.calender = calender;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @XmlTransient
    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @XmlTransient
    public List<Event> getEventList1() {
        return eventList1;
    }

    public void setEventList1(List<Event> eventList1) {
        this.eventList1 = eventList1;
    }

    @XmlTransient
    public List<Event> getEventList2() {
        return eventList2;
    }

    public void setEventList2(List<Event> eventList2) {
        this.eventList2 = eventList2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.se2.wecalevent.entities.User[ userId=" + userId + " ]";
    }

    public String getCalender() {
        return calender;
    }

    public void setCalender(String calender) {
        this.calender = calender;
    }
    
}
