/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.controllers;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;


/**
 *
 * @author Mert
 */
@Named(value = "indexController")
@RequestScoped
public class indexController {

    /**
     * Creates a new instance of indexController
     */
    public indexController() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    private String message = "Hello World from bean:)";
    
    
    
}
