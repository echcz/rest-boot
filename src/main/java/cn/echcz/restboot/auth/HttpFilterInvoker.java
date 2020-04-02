package cn.echcz.restboot.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Builder
public class HttpFilterInvoker {
    @Getter @Setter
    private HttpServletRequest request;
    @Getter @Setter
    private HttpServletResponse response;
    @Getter
    private FilterChain filterChain;
}
