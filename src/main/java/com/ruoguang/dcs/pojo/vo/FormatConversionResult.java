package com.ruoguang.dcs.pojo.vo;

import lombok.Data;

/**
 * 格式转换结果
 */
@Data
public class FormatConversionResult {
    public static final int SUCCESS = 1;
    public static final int FAIL = 0;
    /**
     * 状态
     */
    private int status = SUCCESS;
    /**
     * base64位结果
     */
    private Base64ResultSet base64ResultSet;
    /**
     *
     */
    private String outPath;
    /**
     * 错误信息
     */
    private String errorMsg;

}