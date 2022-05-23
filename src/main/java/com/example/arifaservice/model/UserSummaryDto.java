package com.example.arifaservice.model;

import lombok.Data;

import java.util.List;

@Data
public class UserSummaryDto {
    private String userId;
    private String idNumber;
    private String mobileNumber;
    private String email;
    private String nickname;

    private List<NotificationSetting> notificationSettings;
}
