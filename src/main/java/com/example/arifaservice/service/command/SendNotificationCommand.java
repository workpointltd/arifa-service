package com.example.arifaservice.service.command;

import com.example.arifaservice.model.TransportMedium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Command;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationCommand implements Command {
    private TransportMedium notificationMedium;
    private String title;
    private String body;
    private String phoneNumber;
    private String emailAddress;

}
