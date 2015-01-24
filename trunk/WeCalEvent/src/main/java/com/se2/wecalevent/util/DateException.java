/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

/**
 *
 * @author Mert
 */
public class DateException extends Exception {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public DateException(String message) {
        super(message);
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    
}
