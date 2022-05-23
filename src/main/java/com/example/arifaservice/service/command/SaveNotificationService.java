package com.example.arifaservice.service.command;

import com.example.arifaservice.model.Notification;
import com.example.arifaservice.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.contract.Type;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

@Service
public class SaveNotificationService extends CommandBaseService<SaveNotificationCommand, String> {

    private final NotificationRepository notificationRepository;

    @Autowired
    public SaveNotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public CommandResult<String> execute(SaveNotificationCommand command, Context context) {
        
        var notification = new Notification();
        notification.setNotificationType(command.getNotificationType());
        notification.setTitle(command.getTitle());
        notification.setBody(command.getBody());
        notification.setUserId(command.getUserId());
        notification.setEmailAddress(command.getEmailAddress());
        notification.setPhoneNumber(command.getPhoneNumber());
        notification.setStatus(command.getStatus());
        notification.setErrorCause(command.getErrorCause());
        notification.setCorrelationId(command.getCorrelationId());

        try {
            notification = notificationRepository.save(notification);
        }catch (Exception e){

            var internalMessage = InternalMessage
                    .builder()
                    .message(e.getLocalizedMessage())
                    .type(Type.FAILURE)
                    .isTechnical(true)
                    .httpStatus(500)
                    .code("500")
                    .build();

            return  new CommandResult.Builder<String>()
                    .message(internalMessage)
                    .build();
        }

        return new CommandResult.Builder<String>()
                .ok()
                .id(String.valueOf(notification.getId()))
                .build();
    }

}
