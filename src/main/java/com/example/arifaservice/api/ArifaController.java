package com.example.arifaservice.api;


import com.example.arifaservice.api.body.CreateNotificationTypeBody;
import com.example.arifaservice.api.body.SendNotificationBody;
import com.example.arifaservice.service.command.CreateNotificationFlowCommand;
import com.example.arifaservice.service.command.CreateNotificationTypeCommand;
import com.example.arifaservice.service.command.CreateNotificationTypeService;
import com.example.arifaservice.service.command.ProcessSendNotificationFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rolengi.platform.Context;
import rolengi.platform.api.Controller;
import rolengi.platform.api.ResponseConverter;
import rolengi.platform.contract.Response;

import java.util.Map;

@RestController
@RequestMapping("api/v1/arifa")
public class ArifaController implements Controller {

    private final ProcessSendNotificationFlowService sendNotificationFlowService;
    private final CreateNotificationTypeService createNotificationTypeService;

    @Autowired
    public ArifaController(ProcessSendNotificationFlowService sendNotificationFlowService
            , CreateNotificationTypeService createNotificationTypeService) {
        this.sendNotificationFlowService = sendNotificationFlowService;
        this.createNotificationTypeService = createNotificationTypeService;
    }

    @PostMapping
    public ResponseEntity<Response> sendNotification(@RequestHeader Map<String, String> headers
            , @RequestBody SendNotificationBody sendNotificationBody){
        var context = new Context(headers);
        return new ResponseConverter().convert(
                sendNotificationFlowService.decorate(new CreateNotificationFlowCommand(sendNotificationBody), context)
                , context);
    }

    @PostMapping("notification-type")
    public ResponseEntity<Response> createNotificationType(@RequestHeader Map<String,String> headers
            ,@RequestBody CreateNotificationTypeBody createNotificationTypeBody){
        var context = new Context(headers);
        return new ResponseConverter().convert(createNotificationTypeService.decorate(
                new CreateNotificationTypeCommand(createNotificationTypeBody),context),context);
    }
}
