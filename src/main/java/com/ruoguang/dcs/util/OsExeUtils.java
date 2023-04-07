package com.ruoguang.dcs.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 操作系统执行文件配置工具
 */
@Component
public class OsExeUtils {

    /**
     * windows执行文件
     */
    @Value("${wkhtmltopdf.path.win}")
    private   String windowExePath;
    /**
     * linux执行文件
     */
    @Value("${wkhtmltopdf.path.linux}")
    private   String linuxExePath;


    /**
     * @return
     */
    public  String getWindowExePath() {
        return windowExePath;
    }

    /**
     * @return
     */
    public  String getLinuxExePath() {
        return linuxExePath;
    }

}
