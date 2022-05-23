package com.example.arifaservice.model;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationTemplateBody {
    private String templateType;
    private Map<String, String> parameters;
}
