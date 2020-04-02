package cn.echcz.restboot.auth;

import lombok.Builder;

import java.util.Collection;

@Builder
public class SimpleAuthentication implements Authentication {
    private Object subject;
    private Object credentials;
    private boolean authenticated;
    private String details;
    private Collection<String> authorities;

    @Override
    public Object getSubject() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public Collection<String> getAuthorities() {
        return null;
    }
}
