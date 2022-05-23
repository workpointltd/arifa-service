package com.example.arifaservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rolengi.platform.db.Auditable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Notification extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String notificationType;
    private String title;
    private String body;
    private String phoneNumber;
    private String emailAddress;

    private String status;
    private String errorCause;
    private String correlationId;
}
