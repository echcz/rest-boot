package cn.echcz.restboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class RestDogApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestDogApplication.class, args);
    }

}
