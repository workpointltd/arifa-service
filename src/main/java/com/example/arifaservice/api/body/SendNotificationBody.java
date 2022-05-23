package com.example.arifaservice.api.body;

import com.example.arifaservice.model.NotificationTemplateBody;
import com.example.arifaservice.model.TransportMedium;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class SendNotificationBody {
    private String userId;
    private String notificationType;
    @Enumerated(EnumType.STRING)
    private TransportMedium notificationMedium;
    private String title;
    private NotificationTemplateBody titleTemplate;
    private String body;
    private NotificationTemplateBody bodyTemplate;
    private String phoneNumber;
    private String emailAddress;
}
