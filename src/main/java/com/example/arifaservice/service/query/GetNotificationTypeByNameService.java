package com.example.arifaservice.service.query;

import com.example.arifaservice.model.NotificationTypeDto;
import com.example.arifaservice.repositories.NotificationTypeRepository;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.InternalMessage;
import rolengi.platform.decorator.query.QueryBaseService;
import rolengi.platform.result.QueryResult;

@Service
public class GetNotificationTypeByNameService extends QueryBaseService<GetNotificationTypeByNameQuery
        , NotificationTypeDto> {

    private final NotificationTypeRepository notificationTypeRepository;

    public GetNotificationTypeByNameService(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public QueryResult<NotificationTypeDto> execute(GetNotificationTypeByNameQuery query, Context context) {
        var notificationType = notificationTypeRepository.findByName(query.getName());

        if (notificationType.isEmpty()){
            return new QueryResult
                    .Builder<NotificationTypeDto>()
                    .message(InternalMessage.builder()
                            .message(String.format("Notification type %s not found",query.getName()))
                            .build())
                    .notFound()
                    .build();
        }

        return new QueryResult
                .Builder<NotificationTypeDto>()
                .entity(notificationType.get())
                .ok()
                .build();

    }
}
