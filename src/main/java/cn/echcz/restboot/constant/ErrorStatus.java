package cn.echcz.restboot.constant;


import lombok.Getter;

public enum ErrorStatus {
    UNKNOWN(100, "未知错误"),
    SERVER_ERROR(200, "服务处理有误"),
    SERVER_UNAVAILABLE(201, "系统维护中,暂不可用"),
    CLIENT_ERROR(300, "用户请求有误"),
    NOT_FOUND(301, "用户访问的资源不存在"),
    UNSUPPORT_METHOD(302, "不支持此请求方法"),
    UNAUTHORIZED(310, "用户未通过身份验证"),
    FORBIDDEN(311, "用户无访问此资源的权限"),
    BAD_REQUEST(320, "用户请求非法"),
    ARGUMENT_NOT_VALID(321, "用户请求参数未通过验证"),
    ;

    @Getter
    private int code;
    @Getter
    private String msg;

    ErrorStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
