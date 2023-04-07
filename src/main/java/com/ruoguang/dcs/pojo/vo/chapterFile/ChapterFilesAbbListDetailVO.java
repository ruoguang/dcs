package com.ruoguang.dcs.pojo.vo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用章文件缩略图明细
 */
@Data
@NoArgsConstructor
@ApiModel(value = "用章文件缩略图明细VO")
public class ChapterFilesAbbListDetailVO {
    /**
     * 唯一主键
     */
    @ApiModelProperty(value = "唯一主键（系统标识位）")
    private String pkid;
    /**
     * 下标
     * 0开始
     */
    @ApiModelProperty(value = "下标（0开始）")
    private int index;
    /**
     * 合计页数
     */
    @ApiModelProperty(value = "合计页数")
    private int totalPage;
    /**
     * 当前选中页数
     */
    @ApiModelProperty(value = "当前选中页数")
    private int curPage;
    /**
     * 当前选中页数的缩略图编码
     */
    @ApiModelProperty(value = "当前选中页数的缩略图编码")
    private String curAbbImgBase64code;
    /**
     * 当前合同的标题
     */
    @ApiModelProperty(value = "当前合同的标题")
    private String title;
    /**
     * 当前合同的源编码
     */
    @ApiModelProperty(value = "当前合同的源编码")
    private String sourceBase64code;
    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名")
    private String suffix;
    /**
     * 签署状态
     * 0
     */
    @ApiModelProperty(value = "签署状态（）")
    private int signingStatus;
}
