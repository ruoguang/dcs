package com.ruoguang.dcs.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SerialNumberUtil {
    /**
     * 电银reqNo内部定义
     * @param date
     * @return
     */
    public static String esReqNo(Date date) {
        return serialNo(date, "yyyyMMddHHmmss", 9);
    }


    public static String serialNo(Date date, String format, int randomLen) {
        if (date == null || StringUtils.isBlank(format)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String prefix = sdf.format(date);
        String suffix = randomString("0123456789", randomLen);
        return prefix + suffix;
    }

    public static String randomString(String baseString, int length) {
        if (StrUtil.isEmpty(baseString)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(length);
            if (length < 1) {
                length = 1;
            }
            Random random = new Random();
            int baseLength = baseString.length();

            for (int i = 0; i < length; ++i) {
                int number = random.nextInt(baseLength);
                sb.append(baseString.charAt(number));
            }

            return sb.toString();
        }
    }
}
