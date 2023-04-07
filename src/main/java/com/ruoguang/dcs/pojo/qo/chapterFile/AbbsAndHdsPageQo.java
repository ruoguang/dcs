package com.ruoguang.dcs.pojo.qo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "对应缓存中查高清和缩略图")
public class AbbsAndHdsPageQo  {
    /**
     * 查询id
     */
    @ApiModelProperty(value = "查询id", required = true)
    private String allQueryId;
    /**
     *
     */
    @ApiModelProperty(value = "查询页面（页面超过最大页时为最大页）", required = true, example = "1")
    private int pageNum;
}
