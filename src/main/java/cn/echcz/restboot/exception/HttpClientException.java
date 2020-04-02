package cn.echcz.restboot.exception;

import cn.echcz.restboot.constant.ErrorStatus;
import org.springframework.http.HttpStatus;

public class HttpClientException extends HttpException {

    public HttpClientException(HttpStatus status, int code, String message) {
        super(status, code, message);
    }

    public HttpClientException(HttpStatus status, int code) {
        this(status, code, ErrorStatus.CLIENT_ERROR.getMsg());
    }

    public HttpClientException(HttpStatus status) {
        this(status, ErrorStatus.CLIENT_ERROR.getCode());
    }

    public HttpClientException() {
        this(HttpStatus.BAD_REQUEST);
    }
}
