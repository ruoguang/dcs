package com.ruoguang.dcs.pojo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "html2file请求文件")
public class Html2fileQo {
    /**
     * pageUrl：目标链接，带http的链接，无登陆权限验证
     * fileType：文件类型，1-img,2-pdf,3-markdown,4-word,5-excel
     * fileExt：文件扩展名，图片转化可以转化为不同后缀格式的图片
     * pageHtmlContent：目标页面Html内容，当不能提供pageUrl时，将html内容传入也可以转化，其中引用的css和js需要为带http的路径，不能为相对路径
     */
    public static final String TO_IMG = "1";
    public static final String TO_PDF = "2";
    public static final String TO_MD = "3";
    public static final String TO_WORD = "4";
    public static final String TO_EXCEL = "5";
    /**
     * 目标链接
     * 带http的链接，无登陆权限验证
     */
    @ApiModelProperty(value = "目标链接(带http的链接，无登陆权限验证)",example = "https://www.baidu.com")
    private String pageUrl;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型(1img 2pdf 3markdown 4word 5excel)",example = "2")
    private String fileType;
    /**
     * 文件扩展名
     * 图片转化可以转化为不同后缀格式的图片
     */
    @ApiModelProperty(value = "件扩展名(图片转化可以转化为不同后缀格式的图片)")
    private String fileExt;
    /**
     * 目标页面Html内容
     * 当不能提供pageUrl时，将html内容传入也可以转化，其中引用的css和js需要为带http的路径，不能为相对路径
     */
    @ApiModelProperty(value = "目标页面Html内容(不能含有字体样式，比如font-family:黑体，当不能提供pageUrl时，将html内容传入也可以转化，其中引用的css和js需要为带http的路径，不能为相对路径)",example = "<div>该合同暂无附件！</div>")
    private String pageHtmlContent;
}
