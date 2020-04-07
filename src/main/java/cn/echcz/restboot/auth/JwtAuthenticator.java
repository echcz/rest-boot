package cn.echcz.restboot.auth;

import cn.echcz.restboot.constant.ErrorStatus;
import cn.echcz.restboot.model.Role;
import cn.echcz.restboot.model.User;
import cn.echcz.restboot.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator implements Authenticator {
    @NonNull
    private JwtHandler jwtHandler;
    @NonNull
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            // 如果已经通过认证:
            // 直接返回
            return authentication;
        }
        // 取得并解析jwt，从而取得userId
        Object credentials = authentication.getCredentials();
        Integer userId;
        try {
            userId = jwtHandler.getUserIdFromToken(credentials.toString());
        } catch (Exception e) {
            // 解析失败:
            return getFailAuthentication(null, credentials);
        }
        if (userId != null) {
            // 解析成功:
            // 查看用户id对应的用户是否有效
            User user = userService.findDetailById(userId);
            if (user == null || !user.isEnable()) {
                // 用户无效:
                return getFailAuthentication(null, credentials);
            }
            // 用户有效:
            List<String> authorities = user.getRoles().stream()
                    .map(Role::getName).collect(Collectors.toList());
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
