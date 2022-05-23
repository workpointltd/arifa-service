package com.example.arifaservice.service.command;

import com.example.arifaservice.model.TransportMedium;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

import java.util.stream.Collectors;

@Service
public class ValidateNotificationIfAllowedService extends CommandBaseService<ValidateNotificationCommand
        ,String>{

    @Override
    public CommandResult<String> execute(ValidateNotificationCommand command, Context context) {

        var notificationMedium = command.getTransportMedium();
        var notificationType = command.getNotificationType();
        var userNotificationSettings = command.getNotificationSettings();

        TransportMedium finalMedium;

        if (notificationMedium == null && notificationType.getMandatoryTransportMedium() == null) {
            return new CommandResult//
                    .Builder<String>()//
                    .badRequest(InternalMessage.builder()
                            .message(
                                    "No default notification medium is provided for notification type "
                                            + command.getNotificationType()
                                            + ", one must be provided from input.")
                            .build())
                    .build();
        }

        if (notificationMedium != null){

            var defaultMedium = notificationType.getMandatoryTransportMedium();
            if (notificationMedium.equals(defaultMedium)) finalMedium = notificationMedium;
            else {
                if ((!notificationType.getAlternativeTransportMedia().contains(notificationMedium))) {
                    return new CommandResult
                            .Builder<String>()
                            .badRequest(InternalMessage.builder()
                                    .message(
                                            "Requested notification medium is not supported for the notification type" +
                                                    " " + command.getNotificationType())
                                    .build())
                            .build();
                }else finalMedium = notificationMedium;
            }

        }else finalMedium = notificationType.getMandatoryTransportMedium();

        if (userNotificationSettings != null){

            var settings = userNotificationSettings.stream().filter(notificationSetting -> notificationSetting
                    .getNotificationType().equals(notificationType.getName())
            ).collect(Collectors.toList());

            if (!settings.isEmpty()){

                var requestedMediumSetting = settings.stream().filter(notificationSetting -> notificationSetting
                        .getTransportMedium().equals(finalMedium)).findAny();

                if (requestedMediumSetting.isPresent()){
                    if (!requestedMediumSetting.get().isDisabled()){
                        return new CommandResult
                                .Builder<String>()
                                .badRequest(InternalMessage.builder()
                                        .message(
                                                "Requested notification medium is disabled for the requested user: " +
                                                        " " + command.getNotificationType())
                                        .build())
                                .build();
                    }
                }
            }
        }

        return new CommandResult.Builder<String>()
                .id(finalMedium.name())
                .ok()
                .build();
    }
}
