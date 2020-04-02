package cn.echcz.restboot.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    /**
     * 加密安全码
     */
    @Getter
    private String secret = "cn.echcz.restdog";
    /**
     * 签发人
     */
    @Getter @Setter
    private String issuer = "echcz";
    /**
     * 有效时间(单位为ms)
     */
    @Getter @Setter
    private long maxAge = 1800000;
    /**
     * 加密算法
     */
    @Getter
    private Algorithm algorithm = Algorithm.HMAC256(this.secret);

    public void setSecret(String secret) {
        this.secret = secret;
        this.algorithm = Algorithm.HMAC256(this.secret);
    }
}
