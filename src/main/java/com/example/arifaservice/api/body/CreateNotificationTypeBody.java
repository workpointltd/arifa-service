package com.example.arifaservice.api.body;

import com.example.arifaservice.model.NotificationTypeStatus;
import com.example.arifaservice.model.TransportMedium;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateNotificationTypeBody {

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationTypeStatus status;
    @NotNull
    private String name;
    @NotNull
    private TransportMedium mandatoryTransportMedium;
    private Set<TransportMedium> alternativeTransportMedia;
}
