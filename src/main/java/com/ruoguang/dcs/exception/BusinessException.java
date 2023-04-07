package com.ruoguang.dcs.exception;


import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义 业务异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private int code;
    public String message;

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        this.code = businessExceptionEnum.getCode();
        this.message = businessExceptionEnum.getMessage();
    }


}
