package com.example.arifaservice.service.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Command;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSMSCommand implements Command {
    private String to;
    private String text;
}
