package cn.echcz.restboot.auth;

/**
 * 访问拦截器
 * 用于访问控制
 */
public interface AccessInterceptor<T> {
    /**
     * 应该在受保护的方法调用之前调用
     * @param invoker 调用者
     * @return 拦截状态
     */
    InterceptorStatus<T> beforeAccess(T invoker, Authentication authentication);

    /**
     * 应该在受保护的方法调用之后调用
     * @param status 拦截状态
     * @param returnedObject 受保护的方法的返回结果
     * @return 受保护的方法的最终返回结果
     */
    Object afterAccess(InterceptorStatus<T> status, Object returnedObject);
}
