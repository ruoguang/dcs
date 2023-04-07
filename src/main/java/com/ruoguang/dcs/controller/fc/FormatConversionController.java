package com.ruoguang.dcs.controller.fc;


import com.ruoguang.dcs.model.StandardResqVO;
import com.ruoguang.dcs.pojo.qo.Html2fileQo;
import com.ruoguang.dcs.pojo.qo.Pdf2fileQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.AbbsAndHdsPageQo;
import com.ruoguang.dcs.pojo.qo.chapterFile.NormalBase64FileQo;
import com.ruoguang.dcs.pojo.vo.FormatConversionResult;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbAndHdPageVo;
import com.ruoguang.dcs.pojo.vo.chapterFile.AbbsAndHdsPageVo;
import com.ruoguang.dcs.service.IFormatConversionService;
import com.ruoguang.dcs.service.IOpenClientService;
import com.ruoguang.dcs.service.IWord2PdfService;
import com.ruoguang.dcs.util.Base64Util;
import com.ruoguang.dcs.util.StandardResqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
@RequestMapping("open/fc")
@Api(value = "文件格式转换开放接口", tags = {"文件格式转换开放接口"})
@SuppressWarnings("all")
public class FormatConversionController {

    @Autowired
    private IFormatConversionService formatConversionService;

    @Autowired
    private IOpenClientService openClientService;

    @Autowired
    private IWord2PdfService asposeWordService;

    @PostMapping("pdf2file")
    @ApiOperation(value = "pdf2file")
    public StandardResqVO<FormatConversionResult> pdf2file(@RequestBody Pdf2fileQo pdf2fileQo) throws Exception {
        return StandardResqUtil.success(formatConversionService.pdf2file(pdf2fileQo));
    }

    @GetMapping("/openPdfUrl")
    public void viewUrl(@RequestParam(value = "url") String url, HttpServletResponse response) throws Exception {
        String base64code = openClientService.viewUrlForword(url, response);
        BufferedImage bi = Base64Util.base64ToBufferedImage(base64code);
        ImageIO.write(bi, "JPEG", response.getOutputStream());
    }

    /**
     * 缩略图和详情图
     *
     * @param qo
     * @return
     */
    @PostMapping("abbsAndHdsPage")
    @ApiOperation(value = "缩略图和详情图（缓存）")
    public StandardResqVO<AbbsAndHdsPageVo> abbsAndHdsPage(@RequestBody AbbsAndHdsPageQo qo) throws ExecutionException, InterruptedException {
        return StandardResqUtil.success(formatConversionService.allQueryByKey(qo.getAllQueryId(), qo.getPageNum()));
    }

    /**
     * 缩略图和详情图（缓存状态查询）
     *
     * @param qo
     * @return
     */
    @PostMapping("abbsAndHdsPageState")
    @ApiOperation(value = "缩略图和详情图（缓存状态查询）")
    public StandardResqVO<AbbsAndHdsPageVo> abbsAndHdsPageState(@RequestBody AbbsAndHdsPageQo qo) {
        return StandardResqUtil.success(formatConversionService.allQueryByKeyState(qo.getAllQueryId()));
    }

    /**
     * word2pdf
     * 默认使用asposeWordService转换服务
     *
     * @return
     */
    @PostMapping("/word2pdf")
    @ApiOperation(value = "word2pdf")
    public StandardResqVO<FormatConversionResult> word2pdf(@RequestBody NormalBase64FileQo qo) {
        return StandardResqUtil.success(formatConversionService.word2pdf(asposeWordService, qo));
    }

    /**
     * html页面转文档
     *
     * @param qo
     * @return
     */
    @PostMapping("/html2file")
    @ApiOperation(value = "html2file")
    public StandardResqVO<FormatConversionResult> html2file(@RequestBody Html2fileQo qo) {
        return StandardResqUtil.success(formatConversionService.html2file(qo));
    }

    /**
     * 文件转base64编码(目前运行word|pdf格式，最后输出的pdf格式的编码)
     *
     * @return
     * @throws Exception
     */
    @PostMapping("file2base64code")
    @ApiOperation(value = "文件转base64编码(目前运行word|pdf格式，最后输出的pdf格式的编码)")
    public StandardResqVO<String> file2base64code(@RequestParam("file") MultipartFile file) throws IOException {
        return StandardResqUtil.success(formatConversionService.file2base64code(file));
    }

    /**
     * @return
     * @throws Exception
     */
    @PostMapping("/word2pdfByFile")
    @ApiOperation(value = "word转pdf", produces = "application/octet-stream")
    public void word2pdfByFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        formatConversionService.word2pdf(response, asposeWordService, file);
    }

    /**
     * @return
     * @throws Exception
     */
    @PostMapping("/pdf2wordByFile")
    @ApiOperation(value = "pdf转word", produces = "application/octet-stream")
    public void pdf2wordByFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        formatConversionService.pdf2word(response, asposeWordService, file);
    }

    /**
     * @return
     * @throws Exception
     */
    @PostMapping("/pdf2imgByFile")
    @ApiOperation(value = "pdf转img")
    public AbbAndHdPageVo pdf2wordByFile(@RequestParam("file") MultipartFile file) throws Exception {
        return formatConversionService.abbAndHdPage(file);
    }


}
