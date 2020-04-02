package cn.echcz.restboot.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    @NonNull
    private CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (corsProperties.isEnable()) {
            for (CorsProperties.Mapper mapper : corsProperties.getMappers()) {
                registry.addMapping(mapper.getMapping())
                        .allowedOrigins(mapper.getOrigins())
                        .exposedHeaders(mapper.getExposedHeaders())
                        .allowCredentials(mapper.isAllowCredentials())
                        .allowedMethods(mapper.getMethods())
                        .allowedHeaders(mapper.getHeaders())
                        .maxAge(mapper.getMaxAge());
            }
        }
    }
}
