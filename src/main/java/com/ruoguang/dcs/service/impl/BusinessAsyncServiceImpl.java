package com.ruoguang.dcs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoguang.dcs.async.BusinessAsync;
import com.ruoguang.dcs.constants.RedisPreKey;
import com.ruoguang.dcs.service.IBusinessAsyncService;
import com.ruoguang.dcs.service.IRedisService;
import com.ruoguang.dcs.util.Base64Util;
import com.ruoguang.dcs.util.ImgToolUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BusinessAsyncServiceImpl implements IBusinessAsyncService {
    /**
     * 缩略图默认dpi
     */
    private int ABB_IMG_DPI = 72;
    /**
     * 高清图默认dpi
     */
    private int HD_IMG_DPI = 72 * 3;
    /**
     *
     */
    @Value("${abbsAndHds.expire-time}")
    private long abbsAndHdsExpired;

    @Autowired
    private IRedisService redisService;

    @Autowired
    @Lazy
    private BusinessAsync businessAsync;



    @Value("${abbsAndHds.asyn}")
    private boolean abbsAndHdsAsyn;




    @Override
    public boolean autoPdfParseToAbbsAndHdProcess(String allQueryId, byte[] bytes, int pages, boolean abbsAndHdsAsyn) throws IOException {
        if (StringUtils.isBlank(allQueryId) || bytes == null) {
            return false;
        }

        String redisKey = RedisPreKey.CACHE_PDF_ALLQUERY + allQueryId;

        // 异步任务解析
        if (abbsAndHdsAsyn) {
            long start = System.currentTimeMillis();
            Map<String, Object> map = new ConcurrentHashMap<>(pages * 2 + 5);
            map.put("state", 1);
            map.put("totalPage", pages);
            map.put("processed", 0);
            map.put("time", 0);
            redisService.hmset(redisKey, map);
            redisService.expire(redisKey, abbsAndHdsExpired, TimeUnit.SECONDS);

            Set<Future<Boolean>> set = new HashSet<>(pages);
            for (int pageCounter = 0; pageCounter < pages; pageCounter++) {
                Future<Boolean> booleanFuture = businessAsync.autoPdfParseToAbbsAndHdProcessDetail(redisKey, bytes, pageCounter, map, abbsAndHdsAsyn);
                set.add(booleanFuture);
            }

            while (true) {
                if (set.size() == pages && allDone(set)) {
                    long end = System.currentTimeMillis();
                    map.put("state", 2);
                    map.put("processed", pages);
                    map.put("time", (end - start));
                    redisService.hmset(redisKey, map);


                    log.info("pdf全部解析和上传redis共耗时记录-->uuid:{} , size:{}byte , abbsAndHdsAsyn:{} , pages:{} , time:{}ms , speed:{}ms/p", allQueryId, bytes.length, abbsAndHdsAsyn, pages, (end - start), (end - start) / pages);
                    return true;
                }
            }
            // 同步解析
        } else {
            try (PDDocument document = PDDocument.load(bytes)) {
                long start = System.currentTimeMillis();
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                HashMap<String, Object> map = new HashMap<>(document.getNumberOfPages() * 2 + 5);
                map.put("state", 1);
                map.put("totalPage", document.getNumberOfPages());
                map.put("processed", 0);
                map.put("time", 0);
                redisService.hmset(redisKey, map);


                for (int pageCounter = 0; pageCounter < document.getNumberOfPages(); pageCounter++) {
                    autoPdfParseToAbbsAndHdProcessDetailSyn(redisKey, pdfRenderer, pageCounter);
                }

                long end = System.currentTimeMillis();
                map.put("state", 2);
                map.put("processed", document.getNumberOfPages());
                map.put("time", (end - start));
                redisService.hmset(redisKey, map);


                log.info("pdf全部解析和上传redis共耗时记录-->uuid:{} , size:{}byte , abbsAndHdsAsyn:{} , pages:{} , time:{}ms , speed:{}ms/p", allQueryId, bytes.length, abbsAndHdsAsyn, document.getNumberOfPages(), (end - start), (end - start) / document.getNumberOfPages());
                return true;
            } catch (Exception e) {
                log.error("pdf转换错误：", e);
                return false;
            }

        }
    }

    private boolean allDone(Set<Future<Boolean>> sets) {
        if (sets == null || sets.size() == 0) {
            return false;
        }
        for (Future<Boolean> set : sets) {
            if (!set.isDone()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void autoPdfParseToAbbsAndHdProcessDetailAsyn(String redisKey, byte[] bytes, int pageCounter, Map<String, Object> map) throws IOException {
        try (PDDocument document = PDDocument.load(bytes)) {
            final PDFRenderer pdfRenderer = new PDFRenderer(document);

            long start = System.currentTimeMillis();
            BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, ABB_IMG_DPI, ImageType.RGB);
            ImgToolUtil imgToolUtil = new ImgToolUtil(bim);
            imgToolUtil.resize(ABB_IMG_DPI, ABB_IMG_DPI * bim.getHeight() / bim.getWidth());
            String abb = Base64Util.BufferedImageToBase64(bim);


            BufferedImage bim2 = pdfRenderer.renderImageWithDPI(pageCounter, HD_IMG_DPI, ImageType.RGB);
            ImgToolUtil imgToolUtil2 = new ImgToolUtil(bim2);
            imgToolUtil2.resize(HD_IMG_DPI, HD_IMG_DPI * bim2.getHeight() / bim2.getWidth());
            String hd = Base64Util.BufferedImageToBase64(bim2);

            map.put("abb" + "_" + pageCounter, abb);
            map.put("hd" + "_" + pageCounter, hd);

            long end = System.currentTimeMillis();
            log.info("redisKey->{} , pageCounter->{} , time->{}ms", redisKey, pageCounter, (end - start));
        } catch (Exception e) {
            log.error("解析pdf发生了异常，redisKey->{} , pageCounter->{} , e->{}", redisKey, pageCounter, e);
        }
    }


    @Override
    public void autoPdfParseToAbbsAndHdProcessDetailSyn(String redisKey, PDFRenderer pdfRenderer, int pageCounter) throws IOException {
        try {
            long start = System.currentTimeMillis();
            BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, ABB_IMG_DPI, ImageType.RGB);
            ImgToolUtil imgToolUtil = new ImgToolUtil(bim);
            imgToolUtil.resize(ABB_IMG_DPI, ABB_IMG_DPI * bim.getHeight() / bim.getWidth());
            String abb = Base64Util.BufferedImageToBase64(bim);


            BufferedImage bim2 = pdfRenderer.renderImageWithDPI(pageCounter, HD_IMG_DPI, ImageType.RGB);
            ImgToolUtil imgToolUtil2 = new ImgToolUtil(bim2);
            imgToolUtil2.resize(HD_IMG_DPI, HD_IMG_DPI * bim2.getHeight() / bim2.getWidth());
            String hd = Base64Util.BufferedImageToBase64(bim2);

            Map<String, Object> tmp = new HashMap<>(2);
            tmp.put("abb" + "_" + pageCounter, abb);
            tmp.put("hd" + "_" + pageCounter, hd);
            tmp.put("processed", pageCounter + 1);
            redisService.hmset(redisKey, tmp);

            long end = System.currentTimeMillis();
            log.info("redisKey->{} , pageCounter->{} , time->{}ms", redisKey, pageCounter, (end - start));
        } catch (Exception e) {
            log.error("解析pdf发生了异常，redisKey->{} , pageCounter->{} , e->{}", redisKey, pageCounter, e);
        }
    }

}
