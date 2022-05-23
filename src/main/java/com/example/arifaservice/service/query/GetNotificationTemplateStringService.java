package com.example.arifaservice.service.query;

import com.example.arifaservice.model.CreateTemplateMsgInterRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rolengi.platform.Context;
import rolengi.platform.call.EndpointAdaptor;
import rolengi.platform.call.Method;
import rolengi.platform.call.RolengiClient;
import rolengi.platform.call.auth.AuthorizationMethod;
import rolengi.platform.call.strategy.CallStrategy;
import rolengi.platform.decorator.query.QueryBaseService;
import rolengi.platform.result.CommandResult;
import rolengi.platform.result.QueryResult;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GetNotificationTemplateStringService extends QueryBaseService<GetNotificationTemplateStringQuery, String>{

    private static final String ENDPOINT = "Furushi";

    private static final String SERVICE_NAME = "GET-TEMPLATE-BY-TYPE";

    private static final String SERVICE_URI = "v1/api/template-processes/byType";

    private final CallStrategy callStrategy;

    private final AuthorizationMethod authorizationMethod;

    private final EndpointAdaptor endpointAdaptor;

    public GetNotificationTemplateStringService(@Qualifier("rest") CallStrategy callStrategy
            , @Qualifier("default") AuthorizationMethod authorizationMethod
            ,@Qualifier("inter") EndpointAdaptor endpointAdaptor) {
        this.callStrategy = callStrategy;
        this.authorizationMethod = authorizationMethod;
        this.endpointAdaptor = endpointAdaptor;
    }

    @Override
    public QueryResult<String> execute(GetNotificationTemplateStringQuery query, Context context) {

        var req = new CreateTemplateMsgInterRequest(context.getLanguage(), List.of(query.getNotificationType())
                ,query.getNotificationTemplate().getParameters());
        log.info(req.toString());
        CommandResult<ProcessTemplateDto> result = new RolengiClient.Builder()
                .callStrategy(callStrategy)
                .endpoint(ENDPOINT)
                .serviceName(SERVICE_NAME)
                .endpointAdaptor(endpointAdaptor)
                .authorizationMethod(authorizationMethod)
                .context(context)
                .method(Method.POST)
                .uriFunction(uriBuilder -> uriBuilder
                        .path(SERVICE_URI)
                        .build()
                )
                .body(req)
                .build()
                .exchangeCommand(ProcessTemplateDto.class);

        if (result.isSuccess()) {

            var text = isNullOrEmpty(result.getId().getTemplateText()) ? query.getDefaultMessage()
                    : result.getId().getTemplateText();

            return new QueryResult
                    .Builder<String>()
                    .data(text)
                    .ok()
                    .build();
        }
        return new QueryResult
                .Builder<String>()
                .received(result.getBase())
                .build();
    }

    @Data
    @NoArgsConstructor
    public static class ProcessTemplateDto implements Serializable {

        private static final long serialVersionUID = 1;

        private String templateText;
    }

    private boolean isNullOrEmpty(String s){
        if (Objects.isNull(s)) return true;
        return s.isEmpty();
    }
}
