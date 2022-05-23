package com.example.arifaservice.service.command;

import lombok.Data;
import rolengi.platform.service.Command;

@Data
public class SaveNotificationCommand implements Command {

    private String userId;
    private String notificationType;
    private String title;
    private String body;
    private String phoneNumber;
    private String emailAddress;

    private String status;
    private String errorCause;
    private String correlationId;
}
