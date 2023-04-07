package com.ruoguang.dcs.util;


import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import com.ruoguang.dcs.exception.BusinessException;
import com.ruoguang.dcs.model.ReturnInfo;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * ReturnInfoUtil
 * 全局响应工具
 */
public class ReturnInfoUtil {
    /**
     * success(T data)
     *
     * @param data
     * @return
     */
    public static <T> ReturnInfo<T> success(T data) {
        return new ReturnInfo<T>(0, "success", data);
    }

    /**
     * success()
     *
     * @return
     */
    public static <T> ReturnInfo<T> success() {
        return success(null);
    }

    /**
     * fail()
     *
     * @return
     */
    public static <T> ReturnInfo<T> fail() {
        return fail(null);
    }

    /**
     * fail(T data)
     *
     * @param data
     * @return
     */
    public static <T> ReturnInfo<T> fail(T data) {
        return new ReturnInfo<T>(-1, "fail", data);
    }

    public static <T> ReturnInfo<T> exception(String key, BusinessException be) {
        return new ReturnInfo<T>(be.getCode(), String.format("(%s)%s", key, be.getMessage()), null);
    }

    public static <T> ReturnInfo<T> exception(String key, BusinessExceptionEnum em) {
        return new ReturnInfo<T>(em.getCode(), String.format("(%s)%s", key, em.getMessage()), null);
    }

    public static <T> ReturnInfo<T> exception(MethodArgumentNotValidException me) {
        return new ReturnInfo<T>(BusinessExceptionEnum.REQUEST_PARAMS_ERROE.getCode(), me.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
    }
}
