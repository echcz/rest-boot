package cn.echcz.restboot.auth;

/**
 * 访问条件
 * 用户访问某资源需要满足的条件
 */
public interface AccessCondition {
    /**
     * 根据用户认证信息检查用户是否满足访问条件
     */
    boolean check(Authentication authentication);
}
