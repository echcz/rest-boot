package cn.echcz.restboot.filter;

import cn.echcz.restboot.auth.*;
import cn.echcz.restboot.constant.ErrorStatus;
import cn.echcz.restboot.model.ErrorVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 访问控制过滤器
 */
@RequiredArgsConstructor
@Component
@WebFilter(filterName = "accessFilter", urlPatterns = {"/*"})
@Order(2)
public class AccessFilter implements Filter {
    @NonNull
    private ObjectMapper objectMapper;
    @NonNull
    private AccessInterceptor<HttpFilterInvoker> accessInterceptor;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = AuthContext.getAuthentication();
        if (authentication == null) {
            // AuthContext中没有认证信息，说明不被权限系统跟踪
            // 过滤器放行
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 被权限系统跟踪:
        HttpFilterInvoker invoker = HttpFilterInvoker.builder()
                .request((HttpServletRequest) servletRequest)
                .response((HttpServletResponse) servletResponse)
                .filterChain(chain)
                .build();
        // 访问控制拦截器拦截，判断是否允许访问
        InterceptorStatus<HttpFilterInvoker> status = accessInterceptor.beforeAccess(invoker, authentication);
        if (status.isAllowedAccess()) {
            // 允许访问:
            // 过滤器放行
            HttpFilterInvoker filterInvoker = status.getInvoker();
            chain.doFilter(filterInvoker.getRequest(), filterInvoker.getResponse());
        } else {
            // 不允许访问:
            // 响应异常403
            HttpServletResponse httpResponse = status.getInvoker().getResponse();
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (PrintWriter writer = httpResponse.getWriter()) {
                writer.write(objectMapper.writeValueAsString(
                        ErrorVo.fromErrorStatus(ErrorStatus.valueOf(status.getDetails()))));
            }
        }
        // 拦截器后置处理
        accessInterceptor.afterAccess(status, null);
    }
}
