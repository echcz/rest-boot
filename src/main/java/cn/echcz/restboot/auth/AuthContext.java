package cn.echcz.restboot.auth;

/**
 * 权限上下文
 * 这是线程安全的(使用ThreadLocal保存认证信息)
 */
public class AuthContext {
    private AuthContext() {
        // static method only
    }

    private static final ThreadLocal<Authentication> authenticationThreadLocal = new ThreadLocal<>();

    /**
     * 清除当前上下文内容
     * 应该在当前处理流程完成后调用此方法
     */
    public static void clear() {
        authenticationThreadLocal.remove();
    }

    /**
     * 获取当前上下文的认证信息
     */
    public static Authentication getAuthentication() {
        return authenticationThreadLocal.get();
    }

    /**
     * 将认证信息保存到当前上下文中
     */
    public static void setAuthentication(Authentication authentication) {
        authenticationThreadLocal.set(authentication);
    }

}
