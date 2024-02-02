package allclear.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("AllClear")
                .pathsToMatch("/**")
                .build();
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer").description("액세스 토큰 입력하면됩니다. 큰따옴표 빼고 넣으면 됨");


        return new OpenAPI()
                .info(new Info().title("AllClear API").description("AllClear").version("v0.0.1"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("jwt token", securityScheme))
                .addSecurityItem(new SecurityRequirement().addList("jwt token"));

    }
}
