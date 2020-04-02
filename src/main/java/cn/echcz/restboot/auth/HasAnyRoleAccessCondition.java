package cn.echcz.restboot.auth;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class HasAnyRoleAccessCondition implements AccessCondition {
    private Set<String> roles;

    @Override
    public boolean check(Authentication authentication) {
        if (roles == null || roles.isEmpty()) {
            // 如果当前没有权限要求:
            return true;
        }
        if (authentication.getAuthorities() == null) {
            // 如果当前有权限要求，但用户没有权限:
            return false;
        }
        for (String authority : authentication.getAuthorities()) {
            if (roles.contains(authority)) {
                // 如果有权限要求，且用户有权限:
                // 用户如果满意任一权限要求就通过
                return true;
            }
        }
        return false;
    }
}
