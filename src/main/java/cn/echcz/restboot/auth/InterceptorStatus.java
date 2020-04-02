package cn.echcz.restboot.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 拦截状态
 */
@Builder
public class InterceptorStatus<T> {
    /**
     * 调用者
     */
    @Getter
    private T invoker;
    /**
     * 认证信息
     */
    @Getter
    private Authentication authentication;
    /**
     * 是否允许访问
     */
    @Getter
    private boolean allowedAccess;
    /**
     * 访问控制详情
     * 一般用于表示不允许访问的原因说明
     */
    @Getter
    private String details;

    public InterceptorStatus(T invoker, Authentication authentication, boolean allowedAccess, String details) {
        this.invoker = invoker;
        this.authentication = authentication;
        this.allowedAccess = allowedAccess;
        this.details = details;
    }
}
