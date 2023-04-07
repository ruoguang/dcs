package com.ruoguang.dcs.pojo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * pdf2file请求对象
 */
@Data
@ApiModel(value = "pdf2file请求对象", description = "pdf2file请求对象")
public class Pdf2fileQo {
    /**
     * pdf数据源
     * base64编码方式
     */
    @ApiModelProperty(value = "转化的文件(base64编码,不为空)", required = true)
    private String base64Source;
    /**
     * 源文件名
     */
    @ApiModelProperty(value = "源文件名(不为空)", required = true)
    private String title;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型(1img 2txt 3word)", example = "1")
    private String fileType;
    /**
     * 数据源格式
     */
    @ApiModelProperty(hidden = true)
    private String sourceType;
    /**
     * 转化后的数据文件 bytes
     */
    @ApiModelProperty(hidden = true)
    private byte[] fileBytes;
    /**
     * 输出起始页面
     */
    @ApiModelProperty(value = "输出起始页面(默认0)")
    private int startPage;
    /**
     * 输出终止页面
     */
    @ApiModelProperty(value = "输出终止页面(默认当前pdf的最大页面下标)")
    private int endPage;
    /**
     * 像素
     */
    @ApiModelProperty(value = "像素(单位px 转换的最后像素，像素越大文件内容大，默认2560px)", example = "2560")
    private int dpi;
    /**
     * 转换后的图片格式
     */
    @ApiModelProperty(value = "转换后的图片格式(.png .jpg 等)", example = ".png")
    private String imageType;
    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名")
    private String suffix;


}
