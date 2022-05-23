package com.example.arifaservice.model;

import lombok.Data;

import java.util.Set;

@Data
public class NotificationTypeDto {
    private Long id;
    private NotificationTypeStatus status;
    private String name;
    private TransportMedium mandatoryTransportMedium;
    private Set<TransportMedium> alternativeTransportMedia;
}
