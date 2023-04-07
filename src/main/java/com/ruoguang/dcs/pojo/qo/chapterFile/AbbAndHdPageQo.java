package com.ruoguang.dcs.pojo.qo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 对应pdf的缩略图和高清图
 */
@Data
@ApiModel(value = "对应pdf的缩略图和高清图")
public class AbbAndHdPageQo {
    /**
     * 对应pdf信息
     */
    @ApiModelProperty("对应pdf信息")
    private NormalBase64FileQo base64COde;
    /**
     * 是否查询所有缩略图和高清图
     * 0否1是
     */
    @ApiModelProperty(value = "是否查询所有缩略图和高清图（0否1是，默认0）", example = "1")
    private int allQuery;
}
