package com.example.arifaservice;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import rolengi.platform.PlatformJavaApplication;
import rolengi.platform.call.EndpointAdaptor;
import rolengi.platform.call.auth.token.OauthChecker;
import rolengi.platform.call.auth.token.OauthTokenProvider;
import rolengi.platform.call.auth.token.TokenRefresher;
import rolengi.platform.call.enpoint.inter.InterServiceAdaptor;
import rolengi.platform.call.enpoint.keycloak.KeycloakAdaptor;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@EnableJpaRepositories(basePackages = "com.example.arifaservice.repositories")
public class ArifaServiceApplication extends PlatformJavaApplication {

    private final TaskScheduler taskScheduler;

    public ArifaServiceApplication(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(ArifaServiceApplication.class, args);
    }

    @Bean("inter")
    public EndpointAdaptor interAdaptor() {
        return new InterServiceAdaptor();
    }


    @Bean("keycloak")
    public KeycloakAdaptor keycloakAdaptor() {
        return new KeycloakAdaptor();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TokenRefresher tokenRefresher() {
        return new TokenRefresher(taskScheduler, new OauthTokenProvider());
    }

    @Bean
    public OauthChecker oauthChecker() {
        return new OauthChecker(tokenRefresher());
    }

//    @Bean
//    public JavaMailSender javaMailSender() {
//        return new JavaMailSenderImpl();
//    }
}
