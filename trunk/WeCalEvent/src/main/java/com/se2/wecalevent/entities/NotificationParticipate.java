/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "notification_participate")
@NamedQueries({
    @NamedQuery(name = "NotificationParticipate.findAll", query = "SELECT n FROM NotificationParticipate n"),
    @NamedQuery(name = "NotificationParticipate.findByNotifId", query = "SELECT n FROM NotificationParticipate n WHERE n.notifId = :notifId"),
    @NamedQuery(name = "NotificationParticipate.findByTs", query = "SELECT n FROM NotificationParticipate n WHERE n.ts = :ts")})
public class NotificationParticipate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "notif_id")
    private Integer notifId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "notification")
    private String notification;
    @Column(name = "ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ts;
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    @ManyToOne(optional = false)
    private Event eventId;

    public NotificationParticipate() {
    }

    public NotificationParticipate(Integer notifId) {
        this.notifId = notifId;
    }

    public NotificationParticipate(Integer notifId, String notification) {
        this.notifId = notifId;
        this.notification = notification;
    }

    public Integer getNotifId() {
        return notifId;
    }

    public void setNotifId(Integer notifId) {
        this.notifId = notifId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notifId != null ? notifId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationParticipate)) {
            return false;
        }
        NotificationParticipate other = (NotificationParticipate) object;
        if ((this.notifId == null && other.notifId != null) || (this.notifId != null && !this.notifId.equals(other.notifId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.se2.wecalevent.entities.NotificationParticipate[ notifId=" + notifId + " ]";
    }
    
}
