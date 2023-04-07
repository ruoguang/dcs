package com.ruoguang.dcs.service.impl;

import cn.hutool.core.io.FileUtil;


import com.alibaba.fastjson.JSONObject;
import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import com.ruoguang.dcs.exception.BusinessException;
import com.ruoguang.dcs.model.AjaxResult;
import com.ruoguang.dcs.pojo.qo.PdfMergeQO;
import com.ruoguang.dcs.service.IPdfOperationService;
import com.ruoguang.dcs.util.Base64Util;
import com.ruoguang.dcs.util.OpenClient;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 单独的pdf处理业务层
 *
 * @author cc
 */
@Service
public class PdfOperationServiceImpl implements IPdfOperationService {

    public static final String PDF_MERGE_SUFFIX = "_merge.pdf";

    public static final String TMP_PDF_PATH = "tmp/pdf/";


    @Autowired
    private OpenClient openClient;

    @Override
    public String order2Merge(List<InputStream> inputStreams, String outFileName) throws IOException {
        if (inputStreams.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.INPUTSTREAMS_CAN_NOT_BE_EMPTY);
        }
        PDFMergerUtility ut = new PDFMergerUtility();
        ut.addSources(inputStreams);

        ut.setDestinationFileName(outFileName);
        ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

        return outFileName;
    }

    @Override
    public String order2MergeResult(PdfMergeQO pdfMergeQO, HttpServletResponse response) throws Exception {
        if (pdfMergeQO == null || pdfMergeQO.getOpenUrls().isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.INPUTSTREAMS_CAN_NOT_BE_EMPTY);
        }
        List<String> openUrls = pdfMergeQO.getOpenUrls();
        List<InputStream> inputStreams = new ArrayList<>();
        String fileName = TMP_PDF_PATH + System.currentTimeMillis() + PDF_MERGE_SUFFIX;
        File file = new File(fileName);
        FileUtil.touch(file);
        for (String openUrl : openUrls) {
            AjaxResult ajaxResult = openClient.executeRequest(openUrl, "GET", new JSONObject(), System.currentTimeMillis(), "");
            String pdfBase64Code = ajaxResult.getData().getReposebody();

            InputStream inputStream = Base64Util.base64ToInputStream(pdfBase64Code);

            inputStreams.add(inputStream);
        }
        response.setContentType("application/pdf;charset=UTF-8");
        String pdfMergeResultFilePath = order2Merge(inputStreams, file.getAbsolutePath());
        File fileOut = new File(pdfMergeResultFilePath);
        return Base64Util.file2base64Str(fileOut);
    }
}

