package com.ruoguang.dcs.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ruoguang.dcs.model.AjaxResult;
import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.service.IFormatConversionService;
import com.ruoguang.dcs.service.IOpenClientService;
import com.ruoguang.dcs.util.OpenClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class OpenClientServiceImpl implements IOpenClientService {

    @Autowired
    private OpenClient openClient;
    @Autowired
    private IFormatConversionService formatConversionService;

    @Override
    public String viewUrlForword(String url, HttpServletResponse response) throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        AjaxResult ajaxResult = openClient.executeRequest(url, "GET", new JSONObject(), System.currentTimeMillis(), "");


        String pdfBase64Code = ajaxResult.getData().getReposebody();
        if (!pdfBase64Code.startsWith("data:application/pdf;base64,")) {
            pdfBase64Code = "data:application/pdf;base64,"+pdfBase64Code;
        }
        Pdf2fileQo pdf2fileQo = new Pdf2fileQo();
        pdf2fileQo.setBase64Source(pdfBase64Code);
        FormatConversionResult formatConversionResult = formatConversionService.pdf2file(pdf2fileQo);
        return formatConversionResult.getBase64ResultSet().getBase64RsultDetails().get(0).getBase64Str();
    }
}
