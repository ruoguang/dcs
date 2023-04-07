package com.ruoguang.dcs.pojo.qo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用章文件缩略列表请求对象
 */
@Data
@ApiModel(value = "用章文件缩略列表请求对象")
public class ChapterFilesAbbListQo  {
    @ApiModelProperty(value = "base64编码集合", required = true)
    private List<NormalBase64FileQo> details;
}
