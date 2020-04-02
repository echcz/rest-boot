package cn.echcz.restboot.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "security.auth")
public class AuthProperties {
    /**
     * 是否启用权限系统
     */
    @Getter @Setter
    private boolean enable = false;
    /**
     * 包含token的请求头
     */
    @Getter @Setter
    private String tokenHeader = "Authentication";
    /**
     * 包含userId的jwtKey
     */
    @Getter @Setter
    private String jwtKey = "userId";
    /**
     * 权限系统跟踪路径
     */
    @Getter @Setter
    private List<String> paths = new ArrayList<>();
    /**
     * 权限系统排除跟踪路径
     */
    @Getter @Setter
    private List<String> excludedPaths = new ArrayList<>();
}
