package com.example.arifaservice.service.command;

import com.example.arifaservice.model.NotificationSetting;
import com.example.arifaservice.model.NotificationTypeDto;
import com.example.arifaservice.model.TransportMedium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Command;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateNotificationCommand implements Command {
    private NotificationTypeDto notificationType;
    private TransportMedium transportMedium;
    private List<NotificationSetting> notificationSettings;
}
