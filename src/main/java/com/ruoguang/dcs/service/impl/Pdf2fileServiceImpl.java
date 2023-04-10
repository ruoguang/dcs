package com.ruoguang.dcs.service.impl;


import com.ruoguang.dcs.async.BusinessAsync;
import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.NormalBase64FileQo;
import com.ruoguang.dcs.pojo.vo.Base64ResultSet;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbAndHdPageVo;
import com.ruoguang.dcs.pool.PDFDocumentPool;
import com.ruoguang.dcs.service.IPdf2fileService;
import com.ruoguang.dcs.service.IWord2PdfService;
import com.ruoguang.dcs.util.Base64Util;
import com.ruoguang.dcs.util.ImgToolUtil;
import com.ruoguang.dcs.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;


/**
 * 核心pdf2file的功能实现
 * dpi（点每英寸)dpi越大图片还原度越高，不支持用户定义，响应解析性能或意外崩溃，所以我们在这里定义
 * 缩略图dpi为72
 * 高清图dpi为72
 */
@Service
@Slf4j
public class Pdf2fileServiceImpl implements IPdf2fileService {

    /**
     * 缩略图默认dpi
     */
    private static int ABB_IMG_DPI = 72;
    /**
     * 高清图默认dpi
     */
    private static int HD_IMG_DPI = 300;

    @Autowired
    private BusinessAsync businessAsync;

    @Autowired
    private IWord2PdfService asposeWordService;

    @Value("${abbsAndHds.asyn}")
    private boolean abbsAndHdsAsyn;

    @Override
    public FormatConversionResult autoPdf2file(Pdf2fileQo qo) {
        String base64Source = qo.getBase64Source();
        // 这里做格式校验和转换
        if (StringUtils.isBlank(base64Source)) {
            throw new RuntimeException("数据源不能为空");
        }
        if (containsHead(base64Source)) {
            String head = base64Source.split(",")[0];
            String sourceType = head.split(";")[0].split("/")[1];
            qo.setSourceType(sourceType);
            base64Source = removeHead(base64Source);
        }
        if (StringUtils.isBlank(qo.getImageType()) || !qo.getImageType().startsWith(".")) {
            qo.setImageType(".png");
        }

        qo.setFileBytes(Base64Util.base64ToByteArr(base64Source));
        qo.setBase64Source(base64Source);
        return pdfToImage(qo);
    }


    /**
     * 校验格式 data:application/pdf;base64,xxx
     *
     * @param base64Source
     * @return
     */
    private boolean isRight(String base64Source) {
        if (StringUtils.isBlank(base64Source)) {
            return false;
        }
        String head = base64Source.split(",")[0];
        return head.matches("data:application/pdf;base64");
    }

    private boolean containsHead(String base64Source) {
        return base64Source.startsWith("data");
    }

    private String removeHead(String base64Source) {
        int length = base64Source.split(",")[0].length();
        return base64Source.substring(length + 1);
    }

    @Override
    public FormatConversionResult pdfToImage(Pdf2fileQo qo) {
        FormatConversionResult result = new FormatConversionResult();
        PDDocument document = null;
        byte[] bytes = qo.getFileBytes();
        try {
            document = PDFDocumentPool.borrowObject(bytes);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int dpi = HD_IMG_DPI;
            Base64ResultSet base64ResultSet = new Base64ResultSet();
            for (int pageCounter = 0; pageCounter < document.getNumberOfPages(); pageCounter++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, dpi, ImageType.RGB);
                ImgToolUtil imgToolUtil = new ImgToolUtil(bim);
                imgToolUtil.resize(dpi, dpi * bim.getHeight() / bim.getWidth());
                base64ResultSet.add(Base64Util.BufferedImageToBase64(bim));
            }
            result.setBase64ResultSet(base64ResultSet);
        } catch (Exception e) {
            log.error("pdf转换错误：", e);
        } finally {
            if (document != null) {
                try {
                    PDFDocumentPool.returnObject(document);
                } catch (Exception e) {
                    //
                }
            }
        }
        return result;
    }


    @Override
    public NormalBase64FileQo pdf2ImgHd(NormalBase64FileQo qo) throws Exception {
        Pdf2fileQo pdf2fileQo = new Pdf2fileQo();
        pdf2fileQo.setBase64Source(qo.getBase64Code());
        pdf2fileQo.setDpi(HD_IMG_DPI);
        pdf2fileQo.setTitle(qo.getTitle());
        pdf2fileQo.setSuffix(qo.getSuffix());
        return pdf2ImgHdProcess(pdf2fileQo);
    }

    @Override
    public AbbAndHdPageVo abbAndHdPage(MultipartFile file) throws Exception {
        return abbAndHdPageProcess(file, true);
    }


    private AbbAndHdPageVo abbAndHdPageProcess(MultipartFile file, boolean isAllQuery) throws Exception {

        return abbAndHdPageProcessDetail(file, isAllQuery);
    }

    private AbbAndHdPageVo abbAndHdPageProcessDetail(MultipartFile multipartFile, boolean allQuery) throws IOException {
        AbbAndHdPageVo result = new AbbAndHdPageVo();
        PDDocument document = null;
        try {
            document = PDFDocumentPool.borrowObject(multipartFile.getBytes());
            result.setAllQuery(allQuery ? 1 : 0);
            if (allQuery) {
                String allQueryId = UuidUtils.uuid32();
                result.setAllQueryId(allQueryId);
                businessAsync.autoPdfParseToAbbsAndHds(allQueryId, multipartFile.getBytes(), document.getNumberOfPages(), abbsAndHdsAsyn);
            }
            result.setTotalPage(document.getNumberOfPages());

        } catch (Exception e) {
            log.error("pdf转换错误：", e);
        } finally {
            if (document != null) {
                try {
                    PDFDocumentPool.returnObject(document);
                } catch (Exception e) {
                    //
                }
            }
        }
        return result;
    }

    private NormalBase64FileQo pdf2ImgHdProcess(Pdf2fileQo qo) throws Exception {
        String base64Source = qo.getBase64Source();
        // 这里做格式校验和转换
        if (StringUtils.isBlank(base64Source)) {
            throw new RuntimeException("数据源不能为空");
        }
        if (containsHead(base64Source)) {
            String head = base64Source.split(",")[0];
            String sourceType = head.split(";")[0].split("/")[1];
            qo.setSourceType(sourceType);
            base64Source = removeHead(base64Source);
        }

        if (StringUtils.isBlank(qo.getImageType()) || !qo.getImageType().startsWith(".")) {
            qo.setImageType(".png");
        }
        qo.setBase64Source(base64Source);
        qo.setFileBytes(Base64Util.base64ToByteArr(base64Source));
        return pdf2ImgHdProcessDetail(qo);
    }




    private NormalBase64FileQo pdf2ImgHdProcessDetail(Pdf2fileQo qo) {
        NormalBase64FileQo result = new NormalBase64FileQo();
        byte[] bytes = qo.getFileBytes();
        PDDocument document=null;
        try {
            document=PDFDocumentPool.borrowObject(bytes);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            int dpi = HD_IMG_DPI;
            Base64ResultSet base64ResultSet = new Base64ResultSet();
            for (int pageCounter = 0; pageCounter < document.getNumberOfPages(); pageCounter++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, dpi, ImageType.RGB);
                ImgToolUtil imgToolUtil = new ImgToolUtil(bim);
                imgToolUtil.resize(dpi, dpi * bim.getHeight() / bim.getWidth());
                String imageToBase64 = Base64Util.BufferedImageToBase64(bim);
                base64ResultSet.add(imageToBase64);
                result.setHdDetailPageBase64Code(imageToBase64);
            }
            result.setBase64Code(qo.getBase64Source());

            result.setTitle(qo.getTitle());
            result.setSuffix(qo.getSuffix());
        } catch (Exception e) {
            log.error("pdf转换错误：", e);
        }finally {
            if (document != null) {
                try {
                    PDFDocumentPool.returnObject(document);
                } catch (Exception e) {
                    //
                }
            }
        }
        return result;
    }

    @Override
    public void pdf2word(InputStream inputStream, OutputStream outputStream) throws Exception {
        asposeWordService.pdf2word(inputStream, outputStream);
    }
}
