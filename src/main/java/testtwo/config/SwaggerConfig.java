package testtwo.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        // my model List<LocalDate>
                        this.typeResolver.resolve(List.class, LocalDateTime.class),
                        // alternate for JSON definition
                        this.typeResolver.resolve(List.class, String.class),
                        Ordered.HIGHEST_PRECEDENCE))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
