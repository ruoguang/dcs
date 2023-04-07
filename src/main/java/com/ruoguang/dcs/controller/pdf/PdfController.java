package com.ruoguang.dcs.controller.pdf;


import com.ruoguang.dcs.model.StandardResqVO;
import com.ruoguang.dcs.pojo.qo.PdfMergeQO;
import com.ruoguang.dcs.service.IPdfOperationService;
import com.ruoguang.dcs.util.StandardResqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("open/pdf")
@Api(value = "PDF开放接口", tags = {"PDF开放接口"})
@SuppressWarnings("all")
public class PdfController {
    @Autowired
    private IPdfOperationService pdfOperationService;

    @PostMapping("order2Merge")
    @ApiOperation(value = "order2Merge")
    public StandardResqVO<String> order2MergeResult(@RequestBody PdfMergeQO pdfMergeQO, HttpServletResponse response) throws Exception {
        return StandardResqUtil.success(pdfOperationService.order2MergeResult(pdfMergeQO,response));
    }
}
