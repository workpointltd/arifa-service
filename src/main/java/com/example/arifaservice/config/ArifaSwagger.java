package com.example.arifaservice.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;
import rolengi.platform.api.SwaggerConfig;
import rolengi.platform.constants.ApiSpecs;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class ArifaSwagger extends SwaggerConfig {

    public static final String TITLE = "Arifa services";
    public static final String DESCRIPTION = "Arifa APIs";
    public static final String VERSION = "1.0";

    public Info apiInfo() {
        return new Info().title(TITLE).description(DESCRIPTION).version(VERSION)
                .termsOfService(ApiSpecs.TERMS_AND_CONDITIONS_OF_SERVICE).contact(getContact()).license(getLicense());
    }

    private License getLicense() {
        return new License().name(ApiSpecs.LICENSE).url(ApiSpecs.LICENSE_URL);
    }

    private Contact getContact() {
        return new Contact().name(ApiSpecs.CONTACT_NAME).email(ApiSpecs.EMAIL).url(ApiSpecs.URL);
    }

}
