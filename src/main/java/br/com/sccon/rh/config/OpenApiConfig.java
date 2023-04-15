package br.com.sccon.rh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("!test")
public class OpenApiConfig {

    private static final OpenAPI OPEN_API = new OpenAPI();

    @Bean
    @Scope("application")
    public OpenAPI springOpenAPI(final BuildProperties buildProperties) {
        return OPEN_API.info(new Info()
            .title(buildProperties.get("title"))
            .description(buildProperties.get("description"))
            .version(buildProperties.getVersion())
        );
    }

}
