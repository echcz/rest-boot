package cn.echcz.restboot.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "web.cors")
public class CorsProperties {
    /**
     * 是否启用 CORS 跨域
     */
    @Getter @Setter
    private boolean enable = false;
    @Getter @Setter
    private List<Mapper> mappers = Collections.singletonList(new Mapper());

    @NoArgsConstructor
    public static class Mapper {
        /**
         * 请求地址
         */
        @Getter @Setter
        private String mapping = "/**";
        /**
         * 允许的源列表
         */
        @Getter @Setter
        private String[] origins = {"*"};
        /**
         * 允许的方法列表
         */
        @Getter @Setter
        private String[] methods = {"*"};
        /**
         * 额外允许的请求头列表
         */
        @Getter @Setter
        private String[] headers = {"*"};
        /**
         * 额外公开的响应头列表
         */
        @Getter @Setter
        private String[] exposedHeaders = {};
        /**
         * 是否允许发送cookies
         */
        @Getter @Setter
        private boolean allowCredentials = false;
        /**
         * 预检请求的有效期，以秒为单位
         */
        @Getter @Setter
        private long maxAge = 3600;
    }

}
