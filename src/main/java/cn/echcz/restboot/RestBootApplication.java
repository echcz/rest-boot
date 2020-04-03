package cn.echcz.restboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class RestBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestBootApplication.class, args);
    }

}
