package com.example.arifaservice.interfaces;

import com.example.arifaservice.service.command.SendSMSCommand;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@FunctionalInterface
public interface ISMSProvider {
    ResponseEntity<Map<String, Object>> sendSMS(SendSMSCommand sendSMSCommand);
}
