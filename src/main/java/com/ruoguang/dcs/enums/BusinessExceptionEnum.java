package com.ruoguang.dcs.enums;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 业务异常枚举
 */
@Getter
@ApiModel(value = "自定义业务异常响应枚举")
public enum BusinessExceptionEnum {
    /**
     * 其中XXXX可依据阿里泰山版错误码来构造(非定性要求，业务不一致)
     * <p>
     * code=200：服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。
     * <p>
     * code=4010000：(授权异常) 请求要求身份验证, 客户端需要跳转到登录页面重新登录
     * <p>
     * code=4020000：(凭证过期) 客户端请求刷新凭证接口
     * <p>
     * code=4030000：没有权限禁止访问
     * <p>
     * code=410XXXX：系统主动抛出的业务异常
     * <p>
     * code=5000000：系统异常
     */
    UNAUTHENTICATED(4010000, "登录失效或未登录，请重新登录。"),
    TOKEN_EXPIRED(4020000, "身份过期，请重新登录。"),
    UNAUTHORIZED(4030000, "对不起！你无权访问该接口！"),
    SYSTEM_USER_BAN(40500000, "你已经被系统封禁"),
    SYSTEM_USER_LIMIT(40500001, "你已经被系统限制"),
    MYSQL_ICV_EXCEPTION(4090000, "数据库约束异常"),
    DUPLICATE_KEY_EXCEPTION(4090001, "双重键冲突异常"),
    SYSTEM_ERROE(5000000, "喝口水吧，让数据飞一会，请稍后再试。"),

    INVALID_PHONE_NUMBER(4100151, "无效的手机号码"),
    INVALID_VERIFY_CODE(4100240, "验证码错误！"),
    VERIFY_CODE_ERROR(4100240, "验证码已过期或不存在,请重新获取！"),
    INVALID_VERIFY_CODE_EXIT(4100240, "验证码已存在！"),
    INVALID_USERNAME_PASSWORD(4100210, "无效的用户名和密码！"),
    USERNAME_ALREADY_EXISTS(4100111, "用户名已存在！"),
    PHONE_ALREADY_EXISTS(4100112, "手机号码已存在！"),
    UPDATE_OPERATION_FAIL(4100700, "文件上传异常"),
    DATA_TRANSFER_ERROR(4100900, "数据转换异常"),
    INVALID_FILE_TYPE(4109001, "文件格式不符"),
    REQUEST_PARAMS_ERROE(4109003, "请求参数错误"),
    FILE_UPLOAD_ERROR(4109002, "文件上传异常"),
    SOURCE_IS_NULL(4109004, "暂无此项数据"),
    DATA_NOT_FOUND(4109004, "没有相关数据"),
    NO_REQUEST_METHOD(4109004, "请求方式错误"),
    UPLOAD_FILE_2_BIG(4109006,"上传文件过大" ),
    PASSWORD_ERROR(4109007,"密码错误" ),
    INSERT_ERROR(4109008, "添加失败"),

    STARTDATE_OR_ENDDATE_IS_BLANK(4109009, "开始时间或结束时间一个为空"),
    CONT_STARTSTIME_WHEN_IT_IS_NOT_EFFECTIVE(4109010, "合同开始于未生效时间"),
    PLEASE_FILL_IN_THE_CORRECT_ASSESSMENT_LOGO(4109011, "请填写正确的考核审核标识"),
    CAN_NOT_FIND_THE_START_USING_DATE(4109012, "找不到对应的启用时间"),
    CONTRACTID_CAN_NOT_BE_EMPTY(4109013, "合同id不能为空"),
    INPUTSTREAMS_CAN_NOT_BE_EMPTY(4109014, "输入流不能为空"),



    // 发送合同校验枚举 (段号 [4110000~4120000) )
    CONTRACT_LIST_CANNOT_BE_EMPTY(4110000, "合同列表不能为空"),





    ;

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private int code;
    /**
     * 提示语
     */
    @ApiModelProperty("提示语")
    private String message;



    BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;

    }

}
