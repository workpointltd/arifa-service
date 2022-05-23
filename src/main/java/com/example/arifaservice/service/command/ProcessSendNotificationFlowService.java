package com.example.arifaservice.service.command;

import com.example.arifaservice.model.NotificationSetting;
import com.example.arifaservice.model.TransportMedium;
import com.example.arifaservice.model.UserSummaryDto;
import com.example.arifaservice.service.query.*;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessSendNotificationFlowService extends CommandBaseService<CreateNotificationFlowCommand,String> {

    private final GetUserSummaryInterByIdService getUserSummaryInterByIdService;
    private final GetNotificationTypeByNameService getNotificationTypeByNameService;
    private final ValidateNotificationIfAllowedService validateNotificationIfAllowedService;
    private final GetNotificationTemplateStringService getNotificationTemplateStringService;
    private final SendNotificationExecutorService sendNotificationExecutorService;
    private final SaveNotificationService saveNotificationService;

    public ProcessSendNotificationFlowService(GetUserSummaryInterByIdService getUserSummaryInterByIdService
            , GetNotificationTypeByNameService getNotificationTypeByNameService
            , ValidateNotificationIfAllowedService validateNotificationIfAllowedService
            , GetNotificationTemplateStringService getNotificationTemplateStringService
            , SendNotificationExecutorService sendNotificationExecutorService
            , SaveNotificationService saveNotificationService) {
        this.getUserSummaryInterByIdService = getUserSummaryInterByIdService;
        this.getNotificationTypeByNameService = getNotificationTypeByNameService;
        this.validateNotificationIfAllowedService = validateNotificationIfAllowedService;
        this.getNotificationTemplateStringService = getNotificationTemplateStringService;
        this.sendNotificationExecutorService = sendNotificationExecutorService;
        this.saveNotificationService = saveNotificationService;
    }

    @Override
    public CommandResult<String> execute(CreateNotificationFlowCommand command, Context context) {

        Optional<UserSummaryDto> userSummaryDto = Optional.empty();

        if (Optional.ofNullable(command.getUserId()).isPresent()){
            var userQueryResult = getUserSummaryInterByIdService.decorate(
                    new GetUserSummaryByIdQuery(command.getUserId()),context);

            if (userQueryResult.isFailure()){
                return new CommandResult
                        .Builder<String>()
                        .received(userQueryResult.getBase())
                        .build();
            }

            userSummaryDto = Optional.of(userQueryResult.getData());
        }

        var notificationTypeResult = getNotificationTypeByNameService.decorate(
                new GetNotificationTypeByNameQuery(command.getNotificationType()),context);

        if (notificationTypeResult.isFailure()){
            return new CommandResult
                    .Builder<String>()
                    .received(notificationTypeResult.getBase())
                    .build();
        }

        var notificationType = notificationTypeResult.getData();

        List<NotificationSetting> userNotificationSettings;
        if (userSummaryDto.isPresent()) userNotificationSettings = userSummaryDto.get().getNotificationSettings();
        else userNotificationSettings = Collections.emptyList();

        var validationResult =  validateNotificationIfAllowedService.decorate(
                new ValidateNotificationCommand(notificationType,command.getNotificationMedium()
                        ,userNotificationSettings),context);

        if (validationResult.isFailure()){
            return new CommandResult
                    .Builder<String>()
                    .received(notificationTypeResult.getBase())
                    .build();
        }

        var allowedMedium = TransportMedium.valueOf(validationResult.getId());

        String titleTemplate = null;

        if (command.getTitle() != null && command.getTitleTemplate() != null){
            var titleMessageResult = getNotificationTemplateStringService.decorate(
                    new GetNotificationTemplateStringQuery(command.getTitle()
                            , command.getTitleTemplate().getTemplateId(),allowedMedium
                            , command.getTitleTemplate()),context);

            if (titleMessageResult.isFailure()){
                return new CommandResult
                        .Builder<String>()
                        .received(titleMessageResult.getBase())
                        .build();
            }

            titleTemplate = titleMessageResult.getData();
        }

        var bodyMessageResult = getNotificationTemplateStringService.decorate(
                new GetNotificationTemplateStringQuery(command.getBody(), command.getBodyTemplate().getTemplateId()
                        ,allowedMedium
                        , command.getBodyTemplate()),context);

        if (bodyMessageResult.isFailure()){
            return new CommandResult
                    .Builder<String>()
                    .received(bodyMessageResult.getBase())
                    .build();
        }

        var phoneNumber = isNullOrEmpty(command.getPhoneNumber()) ? userSummaryDto.map(UserSummaryDto::getMobileNumber)
                .orElse(null) : command.getPhoneNumber();
        var emailAddress = isNullOrEmpty(command.getEmailAddress()) ? userSummaryDto.map(UserSummaryDto::getEmail)
                .orElse(null) : command.getEmailAddress();

        var notification = new SaveNotificationCommand();
        notification.setNotificationType(notificationType.getName());
        notification.setTitle(command.getTitle());
        notification.setBody(command.getBody());
        notification.setUserId(command.getUserId());
        notification.setEmailAddress(emailAddress);
        notification.setPhoneNumber(phoneNumber);
        notification.setStatus("SENT");

        var sendNotificationExecutorResult = sendNotificationExecutorService.decorate(
                new SendNotificationCommand(allowedMedium,titleTemplate,bodyMessageResult.getData()
                        ,phoneNumber,emailAddress),context);

        if (sendNotificationExecutorResult.isFailure()){

            notification.setStatus("FAILED");

            var causes =  sendNotificationExecutorResult.getBase();

            var message = causes.getMessages().stream().map(internalMessage ->
                            " [" + internalMessage.getCode() + "] " + internalMessage.getMessage() + " ")
                    .collect(Collectors.joining(" : "));

            notification.setErrorCause(message);

            return new CommandResult
                    .Builder<String>()
                    .received(sendNotificationExecutorResult.getBase())
                    .build();
        }

        var saveNotificationCommand =  saveNotificationService.decorate(
                notification,context);

        if (saveNotificationCommand.getBase().isFailed())
            return new CommandResult.Builder<String>()
                    .received(saveNotificationCommand.getBase())
                    .build();

        return new CommandResult.Builder<String>()
                .id(sendNotificationExecutorResult.getId())
                .ok().build();
    }

    private boolean isNullOrEmpty(String s){
        if (Objects.isNull(s)) return true;
        return s.isEmpty();
    }

}
