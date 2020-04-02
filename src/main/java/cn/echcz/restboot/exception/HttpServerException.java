package cn.echcz.restboot.exception;

import cn.echcz.restboot.constant.ErrorStatus;
import org.springframework.http.HttpStatus;

public class HttpServerException extends HttpException {

    public HttpServerException(HttpStatus status, int code, String message) {
        super(status, code, message);
    }

    public HttpServerException(HttpStatus status, int code) {
        this(status, code, ErrorStatus.SERVER_ERROR.getMsg());
    }

    public HttpServerException(HttpStatus status) {
        this(status, ErrorStatus.SERVER_ERROR.getCode());
    }

    public HttpServerException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
