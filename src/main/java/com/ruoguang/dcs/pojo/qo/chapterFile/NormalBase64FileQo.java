package com.ruoguang.dcs.pojo.qo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 正常的base64请求对象
 */
@Data
@NoArgsConstructor
@ApiModel(value = "正常的base64请求对象")
@AllArgsConstructor
public class NormalBase64FileQo  {
    /**
     * 主键id
     * 可以是合同id等
     */
    @ApiModelProperty(value = "主键id", required = true)
    private String pkId;
    /**
     * 当前选中页
     */
    @ApiModelProperty(value = "当前选中页")
    private int curPage;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题/文件名", required = true)
    private String title;
    /**
     * base64编码
     */
    @ApiModelProperty(value = "base64编码", required = true)
    private String base64Code;
    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名", required = true)
    private String suffix;
    /**
     * 高清详情页base64编码
     */
    @ApiModelProperty(value = "高清详情页base64编码")
    private String hdDetailPageBase64Code;

    public NormalBase64FileQo(String base64Code) {
        this.base64Code = base64Code;
    }
}
