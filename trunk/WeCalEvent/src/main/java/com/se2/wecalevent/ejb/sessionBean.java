/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.ejb;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Mert
 */
@Stateless
public class sessionBean implements sessionBeanRemote {

    @PersistenceContext(unitName = "com.se2_WeCalEvent_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;

    @Override
    public User loginUser(String email, String password) {
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User loggedin = (User) query.getSingleResult();
        if (loggedin.getPass().equals(password)) {
            return loggedin;
        }
        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    @Override
    public boolean register(String email, String password, String calendar, String name, String surname) {
        Query query = entityManager.createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        User loggedin = (User) query.getSingleResult();
        if (loggedin != null) {
            return false;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setPass(password);
        newUser.setCalendar(calendar);
        entityManager.persist(newUser);
        return true;
    }
}
