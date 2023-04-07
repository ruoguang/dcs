package com.ruoguang.dcs.pojo.vo.chapterFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用章文件缩略列表响应对象
 */
@Data
@ApiModel(value = "用章文件缩略列表响应对象")
public class ChapterFilesAbbListVO  {
    /**
     * 总的合同数
     */
    @ApiModelProperty(value = "总的合同数")
    private int totalContCount;
    @ApiModelProperty(value = "合同数缩略列表集合")
    /**
     * 合同数缩略列表集合
     */
    private List<ChapterFilesAbbListDetailVO> details;

    public boolean add(ChapterFilesAbbListDetailVO detail) {
        if (details == null) {
            details = new ArrayList<>();
        }
        detail.setIndex(totalContCount);
        details.add(detail);
        totalContCount++;
        return true;
    }
}
