package com.example.arifaservice.interfaces.impl;

import com.example.arifaservice.interfaces.ISMSProvider;
import com.example.arifaservice.service.command.SendSMSCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class OracomProviderService implements ISMSProvider {

    @Value("${oracom.username}")
    private String oracomUsername;
    @Value("${oracom.password}")
    private String oracomPassword;
    @Value("${oracom.url.send-single-sms}")
    private String sendSingleSms;

    private String authenticationToken()
    {
        String key_secret = oracomUsername + ":" + oracomPassword;
        byte[] bytes = key_secret.getBytes(StandardCharsets.UTF_8);
        return  Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public ResponseEntity<Map<String, Object>> sendSMS(SendSMSCommand sendSMSCommand) {

        //TODO change to RolengiClient
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization","Basic ".concat(authenticationToken()));
        httpHeaders.set("Cache-Control", "no-cache");

        Map<String, Object> response = null;
        try{
            log.info(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(sendSMSCommand));
            HttpEntity<String> httpEntity  = new HttpEntity<>(new ObjectMapper().writer()
                    .withDefaultPrettyPrinter().writeValueAsString(sendSMSCommand), httpHeaders);
            //noinspection unchecked
            response = new RestTemplate().postForObject(sendSingleSms, httpEntity, Map.class);
            //noinspection unchecked
            List<Map<String, Object>>  messages = (List<Map<String, Object>>)
                    Objects.requireNonNull(response).get("messages");
            response = messages.get(0);

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("cause", exception.getCause()));
        }

        return ResponseEntity.ok(response);
    }
}
