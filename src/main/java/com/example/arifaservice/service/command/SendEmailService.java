package com.example.arifaservice.service.command;

import com.example.arifaservice.interfaces.IMailProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.contract.Type;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;;

@Service
public class SendEmailService extends CommandBaseService<SendEmailCommand, String> {

    private final IMailProvider iMailProvider;

    @Autowired
    public SendEmailService(IMailProvider iMailProvider) {
        this.iMailProvider = iMailProvider;
    }

    @Override
    public CommandResult<String> execute(SendEmailCommand command, Context context) {

        try {
            iMailProvider.sendSimpleMail(command.getTo(), command.getSubject(), command.getText()
                    , command.getCc(), command.getBcc());
        }catch (Exception e){
            var internalMessage = InternalMessage.builder()
                    .httpStatus(500)
                    .code("500")
                    .isTechnical(false)
                    .message(e.getLocalizedMessage())
                    .type(Type.FAILURE)
                    .build();

            return new CommandResult.Builder<String>().message(internalMessage).build();
        }

        return new CommandResult.Builder<String>().g("SUCCESS").ok().build();

    }
}
