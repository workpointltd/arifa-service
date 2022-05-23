package com.example.arifaservice.model;

import com.example.arifaservice.service.command.SendSMSCommand;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import rolengi.platform.db.Auditable;

import javax.persistence.Embedded;
import javax.persistence.Id;
import java.util.Map;

@Setter
@Getter
@Document(collection = "SMSs")
public class SMS extends Auditable<String> {

    @Id
    private String id;

    //defaults
    private String from;
    @Indexed
    private String to;
    private String text;

    //Auditing
    private boolean success;
    private String errorCause;

    //metadata
    @Embedded
    private Map<String, Object> metadata;

    public void copyFrom(SendSMSCommand sendSMSCommand){
        setTo(sendSMSCommand.getTo());
        setText(sendSMSCommand.getText());
    }
}
