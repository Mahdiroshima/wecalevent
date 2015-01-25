/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.viewModels;


/**
 *
 * @author Mert
 */
public class NotificationViewModel {
    private NotificationType type;
    public NotificationViewModel(NotificationType type) {
        this.type = type;
    }
    public enum NotificationType {
        Invitation(0),
        Forecast_change(1),
        Postpone_suggestion(2);
        
        private final int value;

        private NotificationType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
