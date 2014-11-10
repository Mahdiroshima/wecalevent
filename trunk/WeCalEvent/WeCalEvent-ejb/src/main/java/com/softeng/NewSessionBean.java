/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softeng;

import Entities.Person;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class NewSessionBean {

   @PersistenceContext
    private EntityManager em;

    public void create(Person person) {
        em.persist(person);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
