package com.ruoguang.dcs.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 标准格式
 *
 * @author cc
 * @date 2021/7/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResqVO<T> {
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String message;
    /**
     * 数据源
     * (T) 泛型不可改，否则swagger2model说明失效
     */
    @ApiModelProperty(value = "数据源")
    private T data;

}
