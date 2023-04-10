package com.ruoguang.dcs.service;


import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.NormalBase64FileQo;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbAndHdPageVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IPdf2fileService {

    FormatConversionResult autoPdf2file(Pdf2fileQo qo) throws Exception;

    FormatConversionResult pdfToImage(Pdf2fileQo qo);


    NormalBase64FileQo pdf2ImgHd(NormalBase64FileQo qo) throws Exception;

    AbbAndHdPageVo abbAndHdPage(MultipartFile file) throws Exception;

    void pdf2word(InputStream inputStream, OutputStream outputStream) throws IOException, Exception;
}
