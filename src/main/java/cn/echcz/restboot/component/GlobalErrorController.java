package cn.echcz.restboot.component;

import cn.echcz.restboot.constant.ErrorStatus;
import cn.echcz.restboot.model.ErrorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@Slf4j
public class GlobalErrorController extends AbstractErrorController {
    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ResponseEntity<ErrorVo> error(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        ErrorVo body;
        switch (status) {
            case NOT_FOUND:
                body = ErrorVo.fromErrorStatus(ErrorStatus.NOT_FOUND);
                break;
            case SERVICE_UNAVAILABLE:
                body = ErrorVo.fromErrorStatus(ErrorStatus.SERVER_UNAVAILABLE);
                break;
            default:
                if (status.value() < 500) {
                    body = ErrorVo.fromErrorStatus(ErrorStatus.CLIENT_ERROR);
                } else {
                    body = ErrorVo.fromErrorStatus(ErrorStatus.SERVER_ERROR);
                }
        }
        return new ResponseEntity<>(body, status);

    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }
}
