package com.example.arifaservice.model;

import lombok.Data;

@Data
public class NotificationSetting {
    private String notificationType;
    private TransportMedium transportMedium;
    private boolean isDisabled;
}
