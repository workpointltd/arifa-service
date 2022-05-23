package com.example.arifaservice.service.command;

import com.example.arifaservice.api.body.CreateNotificationTypeBody;
import com.example.arifaservice.model.NotificationTypeStatus;
import com.example.arifaservice.model.TransportMedium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Command;

import javax.validation.Valid;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationTypeCommand implements Command {
    private NotificationTypeStatus status;
    private String name;
    private TransportMedium mandatoryTransportMedium;
    private Set<TransportMedium> alternativeTransportMedia;

    public CreateNotificationTypeCommand(@Valid CreateNotificationTypeBody body) {
        setName(body.getName());
        setStatus(body.getStatus());
        setAlternativeTransportMedia(body.getAlternativeTransportMedia());
        setMandatoryTransportMedium(body.getMandatoryTransportMedium());
    }
}