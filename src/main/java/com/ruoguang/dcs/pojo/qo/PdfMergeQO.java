package com.ruoguang.dcs.pojo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("pdf合并请参")
public class PdfMergeQO {
    /**
     * 类型
     * 如（openUrls）
     */
    @NotBlank(message = "类型不能为空")
    @ApiModelProperty(value = "类型不能为空（openUrls）", required = true)
    private String type;

    /**
     * openUrls类型请参
     */
    @ApiModelProperty(value = "openUrls类型请参")
    private List<String> openUrls;

}
