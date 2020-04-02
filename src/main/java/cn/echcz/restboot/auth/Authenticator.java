package cn.echcz.restboot.auth;

/**
 * 认证器
 */
public interface Authenticator {
    /**
     * 认证并填充认证信息
     *
     * @return 填充后的认证信息
     */
    Authentication authenticate(Authentication authentication);
}
