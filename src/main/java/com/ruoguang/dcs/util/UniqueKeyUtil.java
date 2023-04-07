package com.ruoguang.dcs.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;

import java.math.BigInteger;

public class UniqueKeyUtil {

    private final static String SSS = "yyyyMMddHHmmssSSS";

    public static String curTimeSSS() {
        return DateUtil.format(DateUtil.date(), SSS);
    }

    public static String curTimeSSSHex() {
        String yyyyMMddHHmmssSSS = DateUtil.format(DateUtil.date(), SSS);
        return HexUtil.toHex(Long.parseLong(yyyyMMddHHmmssSSS));
    }

    public static String parsingKeyToTime(String key) {
        BigInteger bigint = new BigInteger(key, 16);
        String longstr = Long.toString(bigint.longValue());
        DateTime dateTime = DateUtil.parse(longstr, SSS);
        return dateTime.toString();
    }
}
