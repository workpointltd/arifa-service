package com.example.arifaservice.service.command;


import com.example.arifaservice.interfaces.ISMSProvider;
import com.example.arifaservice.model.SMS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.contract.Type;
import rolengi.platform.decorator.command.CommandBaseService;
import rolengi.platform.result.CommandResult;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class SendSMSService extends CommandBaseService<SendSMSCommand, String> {

    private final ISMSProvider ismsProvider;

    @Value("${rolengi.sms.from}")
    private String rolengiIdentifier;

    @Autowired
    public SendSMSService(ISMSProvider ismsProvider) {
        this.ismsProvider = ismsProvider;
    }

    @Override
    public CommandResult<String> execute(SendSMSCommand command, Context context) {
        SMS sms = new SMS();
        sms.setFrom(rolengiIdentifier);
        sms.copyFrom(command);

        ResponseEntity<Map<String, Object>> result =  ismsProvider.sendSMS(command);
        Map<String, Object> metadata = result.getBody();

        sms.setMetadata(metadata);
        if (result.getStatusCode().is2xxSuccessful()){
            sms.setSuccess(true);
        }else {
            sms.setSuccess(false);
            String cause = !Objects.isNull(metadata) ? (String) metadata
                    .getOrDefault("cause", "UNKNOWN") : "UNKNOWN";
            sms.setErrorCause(cause);

            var internalMessage = InternalMessage.builder()
                    .httpStatus(400)
                    .code("400")
                    .isTechnical(false)
                    .message(cause)
                    .type(Type.FAILURE)
                    .build();

            return new CommandResult.Builder<String>().message(internalMessage).build();
        }

        log.info(sms.toString());

        return new CommandResult.Builder<String>().g(sms.getId()).ok().build();
    }
}
