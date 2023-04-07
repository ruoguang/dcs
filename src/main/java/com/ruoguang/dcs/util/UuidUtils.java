package com.ruoguang.dcs.util;

import java.util.UUID;

public class UuidUtils {
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     *
     * @return uuid32:7d4901f285814e04bb814b0f337eb173  32
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
