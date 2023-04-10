package com.ruoguang.dcs.pojo.vo.chapterFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AbbAndHdPageVo {

    /**
     * 合计页数
     */
    @ApiModelProperty(value = "合计页数")
    private int totalPage;

    /**
     * 是否查询所有缩略图和高清图
     * 0否1是
     */
    @ApiModelProperty(value = "是否查询所有缩略图和高清图（0否1是，默认0）", example = "1")
    private int allQuery;
    /**
     * 所有缩略图和高清图Id
     */
    @ApiModelProperty(value = "所有缩略图和高清图Id(仅allQuery=1时才返回)")
    private String allQueryId;



}
