package cn.echcz.restboot.config;

import cn.echcz.restboot.auth.PathAccessConditionMapper;
import cn.echcz.restboot.auth.TreePathAccessConditionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    public PathAccessConditionMapper permissionContext() {
        return new TreePathAccessConditionMapper();
    }
}
