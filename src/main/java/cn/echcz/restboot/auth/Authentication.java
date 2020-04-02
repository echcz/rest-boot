package cn.echcz.restboot.auth;

import java.util.Collection;

/**
 * 认证信息
 */
public interface Authentication {
    /**
     * 获取访问者
     */
    Object getSubject();

    /**
     * 获取访问者凭证
     */
    Object getCredentials();

    /**
     * 是否通过认证
     */
    boolean isAuthenticated();

    /**
     * 认证详情
     * 一般用于表示认证失败的说明
     */
    String getDetails();

    /**
     * 获取访问者的权限
     */
    Collection<String> getAuthorities();
}
