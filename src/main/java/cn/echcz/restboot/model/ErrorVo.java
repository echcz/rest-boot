package cn.echcz.restboot.model;

import cn.echcz.restboot.constant.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorVo {
    public static ErrorVo fromErrorStatus(ErrorStatus errorStatus) {
        return new ErrorVo(errorStatus.getCode(), errorStatus.getMsg());
    }

    /**
     * 错误码
     */
    private int errCode;
    /**
     * 错误说明
     */
    private String errMsg;
}
