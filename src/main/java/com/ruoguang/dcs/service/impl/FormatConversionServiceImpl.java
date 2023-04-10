package com.ruoguang.dcs.service.impl;


import com.ruoguang.dcs.constants.RedisPreKey;
import com.ruoguang.dcs.constants.RegexPatterns;
import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import com.ruoguang.dcs.exception.BusinessException;
import com.ruoguang.dcs.pojo.qo.Html2fileQo;
import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.NormalBase64FileQo;
import com.ruoguang.dcs.pojo.vo.Base64ResultSet;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbAndHdPageVo;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbsAndHdsPageVo;
import com.ruoguang.dcs.service.IFormatConversionService;
import com.ruoguang.dcs.service.IPdf2fileService;
import com.ruoguang.dcs.service.IRedisService;
import com.ruoguang.dcs.service.IWord2PdfService;
import com.ruoguang.dcs.util.Base64Util;
import com.ruoguang.dcs.util.FilesUtils;
import com.ruoguang.dcs.util.PathUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FormatConversionServiceImpl implements IFormatConversionService {


    @Autowired
    private Html2ImageService html2ImageService;
    @Autowired
    private Html2PdfService html2PdfService;
    @Autowired
    private Html2MarkdownService html2MarkdownService;
    @Autowired
    private Html2ExcelService html2ExcelService;
    @Autowired
    private Html2HtmlService html2HtmlService;
    @Autowired
    private Html2WordService html2WordService;
    @Autowired
    private IPdf2fileService pdf2fileService;
    @Autowired
    private IWord2PdfService asposeWordService;
    @Autowired
    private IRedisService redisService;
    @Value("${abbsAndHds.asyn}")
    private boolean abbsAndHdsAsyn;

    @Override
    public FormatConversionResult html2file(Html2fileQo qo) {
        FormatConversionResult result = new FormatConversionResult();
        try {
            if (StringUtils.isBlank(qo.getPageUrl()) && !StringUtils.isBlank(qo.getPageHtmlContent())) {
                qo.setPageHtmlContent(new String(qo.getPageHtmlContent().getBytes(), StandardCharsets.UTF_8));
                //如果只传入了html内容，未传入页面链接（pageUrl），将HTML内容保存为本应用的HTML文档，并且获得http链接地址赋给
                String tempHtmlPath = html2HtmlService.excute(qo.getPageHtmlContent());
                String newPageUrl = PathUtils.getPathBaseClass(tempHtmlPath);
                qo.setPageUrl(newPageUrl);
            }
            String fileRelativePath = null;
            if (Html2fileQo.TO_IMG.equals(qo.getFileType())) {
                fileRelativePath = html2ImageService.excute(qo.getPageUrl(), qo.getFileExt());
            } else if (Html2fileQo.TO_PDF.equals(qo.getFileType())) {
                fileRelativePath = html2PdfService.excute(qo.getPageUrl());
            } else if (Html2fileQo.TO_MD.equals(qo.getFileType())) {
                fileRelativePath = html2MarkdownService.excute(qo.getPageUrl());
            } else if (Html2fileQo.TO_EXCEL.equals(qo.getFileType())) {
                fileRelativePath = html2ExcelService.excute(qo.getPageUrl());
            } else if (Html2fileQo.TO_WORD.equals(qo.getFileType())) {
                fileRelativePath = html2WordService.excute(qo.getPageUrl());
            } else {
                result.setStatus(FormatConversionResult.FAIL);
                fileRelativePath = "暂时不支持该类型文档转化！";
            }
            result.setOutPath(fileRelativePath);
            String absoultOutputPath = PathUtils.getClassRootPath(fileRelativePath);
            File file = cn.hutool.core.io.FileUtil.touch(absoultOutputPath);

            Base64ResultSet base64ResultSet = new Base64ResultSet();
            base64ResultSet.add(Base64Util.file2base64Str(file));
            result.setBase64ResultSet(base64ResultSet);
        } catch (Exception e) {
            log.error("exception e->{}", e);
            result.setStatus(FormatConversionResult.FAIL);
            result.setErrorMsg("解析失败！{" + e.getMessage() + "}");
        }
        return result;
    }

    @Override
    public FormatConversionResult word2pdf(IWord2PdfService word2PdfService, NormalBase64FileQo qo) {
        FormatConversionResult result = new FormatConversionResult();
        try {
            String base64Code = qo.getBase64Code();
            InputStream inputStream = Base64Util.base64ToInputStream(base64Code);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            word2PdfService.word2pdf(inputStream, outputStream);
            byte[] bytes = outputStream.toByteArray();
            Base64ResultSet base64ResultSet = new Base64ResultSet();
            base64ResultSet.add(Base64Util.byteArrToBase64(bytes));
            result.setBase64ResultSet(base64ResultSet);
        } catch (Exception e) {
            log.error("word2pdf exception e->{}", e);
            result.setStatus(FormatConversionResult.FAIL);
            result.setErrorMsg(e.getMessage());
        }
        return result;

    }

    @Override
    public void word2pdf(HttpServletResponse response, IWord2PdfService word2PdfService, MultipartFile file) throws Exception {
        // 首先判断是否word
        boolean word = FilesUtils.isWord(file);
        if (!word) {
            throw new BusinessException(BusinessExceptionEnum.REQUEST_PARAMS_ERROE);
        }
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        word2PdfService.word2pdf(inputStream, outputStream);
        response.setContentType("application/x-download;charset=UTF-8");
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 0);
        String fileName = file.getOriginalFilename().replace(suffix, "");
        String filePDFName = String.format("%s%s.pdf", fileName, System.currentTimeMillis());
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filePDFName, "UTF-8"));
        ServletOutputStream resOutputStream = response.getOutputStream();
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), resOutputStream);
        resOutputStream.flush();
        resOutputStream.close();
        outputStream.close();

    }

    @Override
    public FormatConversionResult pdf2file(Pdf2fileQo qo) throws Exception {
        return pdf2fileService.autoPdf2file(qo);
    }

    @Override
    public void pdf2word(HttpServletResponse response, IWord2PdfService asposeWordService, MultipartFile file) throws Exception {
        // 首先判断是否pdf
        boolean pdf = FilesUtils.isPdf(file);
        if (!pdf) {
            throw new BusinessException(BusinessExceptionEnum.REQUEST_PARAMS_ERROE);
        }
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdf2fileService.pdf2word(inputStream, outputStream);
        response.setContentType("application/x-download;charset=UTF-8");
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 0);
        String fileName = file.getOriginalFilename().replace(suffix, "");
        String filePDFName = String.format("%s%s.docx", fileName, System.currentTimeMillis());
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filePDFName, "UTF-8"));
        ServletOutputStream resOutputStream = response.getOutputStream();
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), resOutputStream);
        resOutputStream.flush();
        resOutputStream.close();
        outputStream.close();
    }


    @Override
    public NormalBase64FileQo hdDetailPage(NormalBase64FileQo qo) throws Exception {
        return pdf2fileService.pdf2ImgHd(qo);
    }

    @Override
    public AbbAndHdPageVo abbAndHdPage(MultipartFile file) throws Exception {
        if (!FilesUtils.isPdf(file)) {
            throw new BusinessException(BusinessExceptionEnum.INVALID_FILE_TYPE);
        }
        return pdf2fileService.abbAndHdPage(file);
    }

    @Override
    public String file2base64code(MultipartFile file) throws IOException {
        // 校验文件类型
        log.info("------ file2base64code -------， file.originalFilename ： {}", file.getOriginalFilename());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        if (StringUtils.isBlank(suffix) || !suffix.matches(RegexPatterns.WORD_AND_PDF_REGEX)) {
            throw new BusinessException(BusinessExceptionEnum.INVALID_FILE_TYPE);
        }
        String curBase64Code = Base64Util.byteArrToBase64(file.getBytes());
        if (suffix.matches(RegexPatterns.PDF_REGEX)) {
            return curBase64Code;
        }
        if (suffix.matches(RegexPatterns.WORD_REGEX)) {
            FormatConversionResult formatConversionResult = word2pdf(asposeWordService, new NormalBase64FileQo(curBase64Code));
            curBase64Code = formatConversionResult.getBase64ResultSet().getBase64RsultDetails().get(0).getBase64Str();
            return curBase64Code;
        }
        return curBase64Code;
    }

    @Override
    public AbbsAndHdsPageVo allQueryByKey(String allQueryId, int pageNum) {
        log.info("thread : {}-->,正在执行 allQueryByKey", Thread.currentThread().getName());
        log.info("allQueryId : {} , pageNum : {}", allQueryId, pageNum);
        AbbsAndHdsPageVo res = new AbbsAndHdsPageVo();
        res.setAllQueryId(allQueryId);
        res.setCurPageNum(pageNum);
        List<Object> list = new ArrayList<>(2);
        Collections.addAll(list,
                "abb_" + pageNum,
                "hd_" + pageNum);

        List<Object> hmget = redisService.hmget(RedisPreKey.CACHE_PDF_ALLQUERY + allQueryId, list);


        String abb = hmget.get(0).toString();
        String hd = hmget.get(1).toString();

        res.setHd(hd);
        res.setAbb(abb);
        res.setAbbsAndHdsAsyn(abbsAndHdsAsyn);

        log.info("allQueryId : {} , pageNum : {}  successed ", allQueryId, pageNum);
        return res;

    }

    @Override
    public AbbsAndHdsPageVo allQueryByKeyState(String allQueryId) {
        AbbsAndHdsPageVo res = new AbbsAndHdsPageVo();
        res.setAllQueryId(allQueryId);
        // 按照顺序返回，没有则为null
        List<Object> list = new ArrayList<>(6);
        Collections.addAll(list,
                "state",
                "totalPage",
                "processed",
                "time");
        List<Object> hmget = redisService.hmget(RedisPreKey.CACHE_PDF_ALLQUERY + allQueryId, list);

        String state = hmget.get(0).toString();
        String totalPage = hmget.get(1).toString();
        String processed = hmget.get(2).toString();
        String time = hmget.get(3).toString();


        Long expire = redisService.getExpire(RedisPreKey.CACHE_PDF_ALLQUERY + allQueryId, TimeUnit.SECONDS);

        res.setTotalPage(Integer.parseInt(totalPage));
        double process = (Integer.parseInt(processed) * 100.0 / Integer.parseInt(totalPage));
        res.setProcess(Double.parseDouble(String.format("%.2f", process)));
        res.setState(Integer.parseInt(state));
        res.setExpireTime(expire);
        res.setTime(Long.parseLong(time));
        res.setAbbsAndHdsAsyn(abbsAndHdsAsyn);
        return res;
    }
}
