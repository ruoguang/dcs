package com.ruoguang.dcs.constants;

/**
 * RegexPatterns
 * 全局正则校验常量
 *
 * @author cc
 * @date 2020/06/06
 */
public abstract class RegexPatterns {
    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^(?:(?:\\+|00)86)?1\\d{10}$";
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 用户名正则
     */
    public static final String USERNAME_REGEX = "^\\w{4,32}$";
    /**
     * 验证码正则
     */
    public static final String CODE_REGEX = "^\\d{6}$";
    /**
     * base64编码
     */
    public static final String BASE64_CODE = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";


    /**
     * word和pdf格式
     */
    public static final String WORD_AND_PDF_REGEX = "^doc|docx|dotx|dot|docm|dotm|pdf$";
    /**
     * word格式
     */
    public static final String WORD_REGEX = "^doc|docx|dotx|dot|docm|dotm$";
    /**
     * pdf格式
     */
    public static final String PDF_REGEX = "^pdf$";
}