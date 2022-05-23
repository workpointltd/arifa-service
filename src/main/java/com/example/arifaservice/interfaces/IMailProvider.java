package com.example.arifaservice.interfaces;

import java.util.List;

public interface IMailProvider{
     void sendSimpleMail(String to, String subject, String text, List<String> cc, List<String> bcc);
}
