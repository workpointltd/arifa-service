package com.example.arifaservice.service.query;

import com.example.arifaservice.model.NotificationTemplate;
import com.example.arifaservice.model.TransportMedium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Query;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNotificationTemplateStringQuery implements Query {
    private String defaultMessage;
    private String notificationType;
    private TransportMedium transportMedium;
    private NotificationTemplate notificationTemplate;
}
