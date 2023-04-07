package com.ruoguang.dcs.model;


import com.ruoguang.dcs.pojo.vo.ResponseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 操作消息提醒
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResult {

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private ResponseVO data;
    private String downloadPath;


    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static AjaxResult error() {
        return error(1, "操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg) {
        return error(500, msg);
    }

    /**
     * 返回错误消息
     *
     * @param code 错误码
     * @param msg  内容
     * @return 错误消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return new AjaxResult(0, msg, null, null);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

}
