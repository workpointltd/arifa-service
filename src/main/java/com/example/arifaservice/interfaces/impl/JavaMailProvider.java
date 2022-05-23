package com.example.arifaservice.interfaces.impl;

import com.example.arifaservice.interfaces.IMailProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Service
public class JavaMailProvider implements IMailProvider {

    private final JavaMailSender javaMailSender;

    @Autowired
    public JavaMailProvider(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @SneakyThrows
    @Override
    public void sendSimpleMail(String to, String subject, String text, List<String> cc, List<String> bcc) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        message.setFrom("system-alerts@rolengi.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text,true);

        if (isNotNullAndEmpty(cc)) message.setCc(cc.toArray(new String[0]));
        if (isNotNullAndEmpty(bcc)) message.setCc(bcc.toArray(new String[0]));

        javaMailSender.send(mimeMessage);
    }

    public boolean isNotNullAndEmpty(List<String> list){
        if (Objects.isNull(list)) return true;
        return list.isEmpty();
    }
}
