package com.example.arifaservice.service.command;

import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

import java.util.Collections;

@Service
public class SendNotificationExecutorService extends CommandBaseService<SendNotificationCommand,String> {

    private final SendEmailService sendEmailService;
    private final SendSMSService sendSMSService;

    public SendNotificationExecutorService(SendEmailService sendEmailService, SendSMSService sendSMSService) {
        this.sendEmailService = sendEmailService;
        this.sendSMSService = sendSMSService;
    }

    @Override
    public CommandResult<String> execute(SendNotificationCommand command, Context context) {

        var transactionMedium = command.getNotificationMedium();

        CommandResult<String> commandResult = null;

        switch (transactionMedium){
            case SMS: commandResult = sendSMSNotification(command, context);
            break;
            case EMAIL: commandResult = sendEmailNotification(command, context);
            break;
        }

        if (commandResult.isFailure()){
            return new CommandResult
                    .Builder<String>()
                    .received(commandResult.getBase())
                    .build();
        }

        return new CommandResult.Builder<String>().id(commandResult.getId()).ok().build();
    }

    private CommandResult<String> sendEmailNotification(SendNotificationCommand command, Context context) {

        if (command.getEmailAddress().isEmpty() || command.getEmailAddress() == null){
            return new CommandResult
                    .Builder<String>()
                    .badRequest(InternalMessage.builder()
                            .message("Email address required to send email notification")
                            .build())
                    .build();
        }

        return sendEmailService.decorate(new SendEmailCommand(
                null, command.getEmailAddress(), command.getBody(), command.getTitle(),
                Collections.emptyList(),Collections.emptyList()),context);
    }

    private CommandResult<String> sendSMSNotification(SendNotificationCommand command, Context context) {

        if (command.getPhoneNumber().isEmpty() || command.getPhoneNumber() == null){
            return new CommandResult
                    .Builder<String>()
                    .badRequest(InternalMessage.builder()
                            .message("Phone number required to send sms notification")
                            .build())
                    .build();
        }

        return sendSMSService.decorate(new SendSMSCommand(command.getPhoneNumber(), command.getBody()),context);
    }
}
