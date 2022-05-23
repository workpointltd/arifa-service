package com.example.arifaservice.service.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rolengi.platform.service.Command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailCommand implements Command {

    private String from;

    @NotNull
    @NotBlank
    private String to;

    @NotNull
    @NotBlank
    private String text;

    private String subject;

    private List<String> cc;

    private List<String> bcc;
}
