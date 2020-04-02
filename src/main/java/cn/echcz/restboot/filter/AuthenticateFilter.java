package cn.echcz.restboot.filter;

import cn.echcz.restboot.auth.AuthContext;
import cn.echcz.restboot.auth.Authentication;
import cn.echcz.restboot.auth.Authenticator;
import cn.echcz.restboot.auth.SimpleAuthentication;
import cn.echcz.restboot.config.AuthProperties;
import cn.echcz.restboot.constant.ErrorStatus;
import cn.echcz.restboot.model.ErrorVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 权限认证过滤器
 */
@RequiredArgsConstructor
@Component
@WebFilter(filterName = "authenticateFilter", urlPatterns = {"/*"})
@Order(1)
public class AuthenticateFilter implements Filter {
    @NonNull
    private ObjectMapper objectMapper;
    @NonNull
    private AuthProperties authProperties;
    @NonNull
    private PathMatcher pathMatcher;
    @NonNull
    private Authenticator authenticator;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 判断当前请求是否由权限系统跟踪
        // 规则是请求URI在AuthProperties.paths但不在AuthProperties.excludePaths里(使用Ant风格的路径匹配)
        String requestURI = request.getRequestURI();
        List<String> excludedPaths = authProperties.getExcludedPaths();
        for (String excludedPath : excludedPaths) {
            if (pathMatcher.match(excludedPath, requestURI)) {
                // 不被权限系统跟踪
                // 请求-响应完成后，需要清理AuthContext，下同
                filterChain.doFilter(servletRequest, servletResponse);
                AuthContext.clear();
                return;
            }
        }
        List<String> paths = authProperties.getPaths();
        for (String path : paths) {
            if (pathMatcher.match(path, requestURI)) {
                // 被权限系统跟踪
                String token = request.getHeader(authProperties.getTokenHeader());
                SimpleAuthentication authentication = SimpleAuthentication.builder()
                        .authenticated(false)
                        .credentials(token)
                        .build();
                // 执行权限认证
                Authentication finalAuthentication = authenticator.authenticate(authentication);
                if (finalAuthentication.isAuthenticated()) {
                    // 权限认证通过:
                    // 将认证信息保存到AuthContext中，过滤器放行
                    AuthContext.setAuthentication(finalAuthentication);
                    filterChain.doFilter(servletRequest, servletResponse);
                    AuthContext.clear();
                } else {
                    // 权限认证没通过:
                    // 返回异常响应401
                    // 因为绝对没有设置过AuthContext，故不需要清理AuthContext
                    HttpServletResponse response = (HttpServletResponse) servletResponse;
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    try (PrintWriter writer = servletResponse.getWriter()) {
                        writer.write(objectMapper.writeValueAsString(
                                ErrorVo.fromErrorStatus(ErrorStatus.valueOf(finalAuthentication.getDetails()))));
                    }
                }
                return;
            }
        }
        // 不被权限系统跟踪
        filterChain.doFilter(servletRequest, servletResponse);
        AuthContext.clear();
    }

}
