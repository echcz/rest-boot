package cn.echcz.restboot.auth;

import cn.echcz.restboot.constant.ErrorStatus;
import cn.echcz.restboot.util.PathUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HttpFilterAccessInterceptor implements AccessInterceptor<HttpFilterInvoker> {
    @NonNull
    private PathAccessConditionMapper pathAccessConditionMapper;

    @Override
    public InterceptorStatus<HttpFilterInvoker> beforeAccess(HttpFilterInvoker invoker, Authentication authentication) {
        // 通过路径找到权限要求
        HttpServletRequest request = invoker.getRequest();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        List<String> path = PathUtils.pathStrToList(requestURI);
        path.add(method);
        AccessCondition condition = pathAccessConditionMapper.getConditionByPath(path);
        if (condition == null || condition.check(authentication)) {
            // 如果没有权限要求或满足权限要求:
            // 返回允许访问状态
            return InterceptorStatus.<HttpFilterInvoker>builder()
                    .invoker(invoker)
                    .authentication(authentication)
                    .allowedAccess(true)
                    .build();
        }
        // 否则返回不允许访问状态
        return InterceptorStatus.<HttpFilterInvoker>builder()
                .invoker(invoker)
                .authentication(authentication)
                .allowedAccess(false)
                .details(ErrorStatus.FORBIDDEN.name())
                .build();
    }

    @Override
    public Object afterAccess(InterceptorStatus<HttpFilterInvoker> status, Object returnedObject) {
        // 不做后置处理
        return returnedObject;
    }
}
