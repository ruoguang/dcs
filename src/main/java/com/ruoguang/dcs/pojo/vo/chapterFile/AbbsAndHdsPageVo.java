package com.ruoguang.dcs.pojo.vo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "高清图集和缩略图集")
public class AbbsAndHdsPageVo {
    /**
     * 查询id
     */
    @ApiModelProperty(value = "查询id")
    private String allQueryId;
    /**
     * pdf总页数
     */
    @ApiModelProperty(value = "pdf总页数")
    private int totalPage;
    /**
     * 0新建 1进行中 2已完成
     */
    @ApiModelProperty(value = "状态（0暂无 1进行中 2已完成）")
    private int state;
    /**
     * 进度
     */
    @ApiModelProperty(value = "进度")
    private double process;
    /**
     * 高清图集(不展示，内容过大)
     */
    @ApiModelProperty(value = "高清图集(不展示，内容过大)")
    private List<String> abbs;
    /**
     * 缩略图集(不展示，内容过大)
     */
    @ApiModelProperty(value = "缩略图集(不展示，内容过大)")
    private List<String> hds;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private int curPageNum;
    /**
     * 当前页缩略图
     */
    @ApiModelProperty(value = "当前页缩略图")
    private String abb;
    /**
     * 当前页高清图
     */
    @ApiModelProperty(value = "当前页高清图")
    private String hd;
    /**
     * 全部解析哦完成时间（单位ms）
     */
    @ApiModelProperty(value = "全部解析哦完成时间（单位ms）")
    private long time;
    /**
     * 过期时间(单位s)
     */
    @ApiModelProperty(value = "过期时间(单位s)，-1标识永不过期")
    private long expireTime = -1;
    /**
     * 是否异步解析
     */
    @ApiModelProperty(value = "是否异步解析")
    private boolean abbsAndHdsAsyn;
}
