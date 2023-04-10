package com.ruoguang.dcs.service;


import com.ruoguang.dcs.pojo.qo.Html2fileQo;
import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.NormalBase64FileQo;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbAndHdPageVo;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbsAndHdsPageVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 格式转换服务
 */
public interface IFormatConversionService {
    /**
     * html2pdf
     *
     * @param qo
     * @return
     */
    FormatConversionResult html2file(Html2fileQo qo);

    /**
     * word2pdf
     *
     * @param word2PdfService
     * @param qo
     * @return
     */
    FormatConversionResult word2pdf(IWord2PdfService word2PdfService, NormalBase64FileQo qo);

    void word2pdf(HttpServletResponse response,IWord2PdfService word2PdfService, MultipartFile file) throws Exception;


    FormatConversionResult pdf2file(Pdf2fileQo qo) throws Exception;

    void pdf2word(HttpServletResponse response, IWord2PdfService asposeWordService, MultipartFile file) throws Exception;

    NormalBase64FileQo hdDetailPage(NormalBase64FileQo qo) throws Exception;

    AbbAndHdPageVo abbAndHdPage(MultipartFile file) throws Exception;

    String file2base64code(MultipartFile file) throws IOException;

    AbbsAndHdsPageVo allQueryByKey(String allQueryId, int pageNum) throws ExecutionException, InterruptedException;

    AbbsAndHdsPageVo allQueryByKeyState(String allQueryId);



}
