package com.ruoguang.dcs.pojo.vo.chapterFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AbbAndHdPageVo {
    /**
     * 唯一主键
     */
    @ApiModelProperty(value = "唯一主键（系统标识位）")
    private String pkid;

    /**
     * 合计页数
     */
    @ApiModelProperty(value = "合计页数")
    private int totalPage;

    /**
     * 当前选中页数的缩略图编码
     */
    @ApiModelProperty(value = "当前选中页数的缩略图编码")
    private String curAbbImgBase64code;
    /**
     * 当前选中页数的高清图编码
     */
    @ApiModelProperty(value = "当前选中页数的高清图编码")
    private String curHdImgBase64code;

    /**
     * 当前合同的源编码
     */
    @ApiModelProperty(value = "当前合同的源编码")
    private String sourceBase64code;

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
