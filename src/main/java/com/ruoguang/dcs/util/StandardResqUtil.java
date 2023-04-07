package com.ruoguang.dcs.util;


import com.ruoguang.dcs.model.StandardResqVO;

/**
 * 标准RespVo工具
 *
 * @author cc
 * @date 2021/08/06
 */
public class StandardResqUtil {
    /**
     * success(T data)
     *
     * @param data
     * @return
     */
    public static <T> StandardResqVO<T> success(T data) {
        return new StandardResqVO<T>("0", "success", data);
    }

    /**
     * success()
     *
     * @return
     */
    public static <T> StandardResqVO<T> success() {
        return success(null);
    }

    /**
     * fail()
     *
     * @return
     */
    public static <T> StandardResqVO<T> fail() {
        return fail(null);
    }

    /**
     * fail(T data)
     *
     * @param data
     * @return
     */
    public static <T> StandardResqVO<T> fail(T data) {
        return new StandardResqVO<T>("-1", "fail", data);
    }
}
