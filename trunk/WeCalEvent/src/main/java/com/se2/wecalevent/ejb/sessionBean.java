/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.ejb;

import com.se2.wecalevent.entities.User;
import com.se2.wecalevent.remote.sessionBeanRemote;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;

/**
 *
 * @author Mert
 */
@Stateless
public class sessionBean implements sessionBeanRemote {

    @PersistenceContext(unitName = "com.se2_WeCalEvent_war_1.0-SNAPSHOTPU")
    private EntityManager entityManager;

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
}
