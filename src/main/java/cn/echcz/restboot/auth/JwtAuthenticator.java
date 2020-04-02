package cn.echcz.restboot.auth;

import cn.echcz.restboot.constant.ErrorStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator implements Authenticator {
    @NonNull
    private JwtHandler jwtHandler;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            // 如果已经通过认证:
            // 直接返回
            return authentication;
        }
        // 取得并解析jwt，从而取得userId
        Object credentials = authentication.getCredentials();
        Long userId;
        try {
            userId = jwtHandler.getUserIdFromToken(credentials.toString());
        } catch (Exception e) {
            // 解析失败:
            return getFailAuthentication(null, credentials);
        }
        if (userId != null) {
            // 解析成功:
            // TODO 验证userId是否有效
            ArrayList<String> authorities = new ArrayList<>();
            // TODO 获取用户权限并填充 authorities
            return SimpleAuthentication.builder()
                    .subject(userId)
                    .credentials(credentials)
                    .authenticated(true)
                    .authorities(authorities)
                    .build();
        }
        return getFailAuthentication(userId, credentials);
    }

    private Authentication getFailAuthentication(Object subject, Object credentials) {
        return SimpleAuthentication.builder()
                .subject(subject)
                .credentials(credentials)
                .authenticated(false)
                .details(ErrorStatus.UNAUTHORIZED.name())
                .build();
    }
}
