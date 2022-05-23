package com.example.arifaservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rolengi.platform.data.Transferable;
import rolengi.platform.db.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
public class NotificationType extends Auditable<String> implements Transferable<NotificationTypeDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private NotificationTypeStatus status;

    @Column(length = 100)
    private String name;

    @NotNull
    private TransportMedium defaultTransportMedium;

    @ElementCollection
    private Set<TransportMedium> possibleTransportMedia;

    @Override
    public NotificationTypeDto toDTO() {
        var dto = new NotificationTypeDto();
        dto.setId(getId());
        dto.setStatus(status);
        dto.setName(name);
        dto.setMandatoryTransportMedium(defaultTransportMedium);
        dto.setAlternativeTransportMedia(possibleTransportMedia);
        return dto;
    }
}
