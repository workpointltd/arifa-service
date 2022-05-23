package com.example.arifaservice.service.command;

import com.example.arifaservice.api.body.SendNotificationBody;
import com.example.arifaservice.model.NotificationTemplate;
import com.example.arifaservice.model.NotificationTemplateBody;
import com.example.arifaservice.model.TransportMedium;
import com.example.arifaservice.utils.StringUtils;
import lombok.Data;
import rolengi.platform.service.Command;

import java.util.Objects;

@Data
public class CreateNotificationFlowCommand implements Command {

    private String userId;
    private String notificationType;
    private TransportMedium notificationMedium;
    private String title;
    private NotificationTemplate titleTemplate;
    private String body;
    private NotificationTemplate bodyTemplate;
    private String phoneNumber;
    private String emailAddress;

    public CreateNotificationFlowCommand(SendNotificationBody body) {
        setUserId(body.getUserId());
        setNotificationType(body.getNotificationType());
        setNotificationMedium(body.getNotificationMedium());
        setTitle(body.getTitle());
        setBody(body.getBody());
        setBodyTemplate(resolveTemplate(body.getBodyTemplate()));
        setTitleTemplate(resolveTemplate(body.getTitleTemplate()));
        setPhoneNumber(StringUtils.formatPhoneNumber(body.getPhoneNumber()));
        setEmailAddress(body.getEmailAddress());
    }

    private NotificationTemplate resolveTemplate(NotificationTemplateBody notificationTemplateBody){
        if (Objects.isNull(notificationTemplateBody)) return null;
        return new NotificationTemplate(notificationTemplateBody.getTemplateType()
                ,notificationTemplateBody.getParameters());
    }
}
