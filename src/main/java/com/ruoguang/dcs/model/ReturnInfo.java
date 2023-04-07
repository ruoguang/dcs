package com.ruoguang.dcs.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * ReturnInfo
 * 全局数据响应格式 完全封装
 *
 * @author cc
 * @date 2020/06/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnInfo<T> {
    @ApiModelProperty("响应状态码")
    private int code;
    @ApiModelProperty("响应提示语")
    private String message;
    @ApiModelProperty("响应数据体")
    private T data;

}
