package com.example.arifaservice.service.command;

import com.example.arifaservice.model.NotificationType;
import com.example.arifaservice.repositories.NotificationTypeRepository;
import com.example.arifaservice.service.query.GetNotificationTypeByNameQuery;
import com.example.arifaservice.service.query.GetNotificationTypeByNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

@Service
public class CreateNotificationTypeService extends CommandBaseService<CreateNotificationTypeCommand,String> {

    private final NotificationTypeRepository notificationTypeRepository;
    private final GetNotificationTypeByNameService getNotificationTypeByNameService;

    @Autowired
    public CreateNotificationTypeService(NotificationTypeRepository notificationTypeRepository
            , GetNotificationTypeByNameService getNotificationTypeByNameService) {
        this.notificationTypeRepository = notificationTypeRepository;
        this.getNotificationTypeByNameService = getNotificationTypeByNameService;
    }

    @Override
    public CommandResult<String> execute(CreateNotificationTypeCommand command, Context context) {

        var existingNotificationType = getNotificationTypeByNameService.decorate(
                new GetNotificationTypeByNameQuery(command.getName()),context);

        if (existingNotificationType.getBase().isSuccess())
            return new CommandResult.Builder<String>()
                    .badRequest(InternalMessage.builder()
                            .message(String.format("Notification type with name %s already exists",command.getName()))
                            .build())
                            .build();

        var notificationType = new NotificationType();
        notificationType.setName(command.getName());
        notificationType.setStatus(command.getStatus());
        notificationType.setDefaultTransportMedium(command.getMandatoryTransportMedium());
        notificationType.setPossibleTransportMedia(command.getAlternativeTransportMedia());

        var savedNotification = notificationTypeRepository.save(notificationType);

        return new CommandResult.Builder<String>()
                .id(savedNotification.getId().toString())
                .created().build();
    }
}
