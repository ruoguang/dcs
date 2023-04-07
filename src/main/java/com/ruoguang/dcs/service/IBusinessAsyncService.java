package com.ruoguang.dcs.service;



import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.IOException;

import java.util.Map;

public interface IBusinessAsyncService {
    boolean autoPdfParseToAbbsAndHdProcess(String allQueryId, byte[] bytes, int pages, boolean abbsAndHdsAsyn) throws IOException;

    void autoPdfParseToAbbsAndHdProcessDetailAsyn(String redisKey, byte[] bytes, int pageCounter, Map<String, Object> detailsMap) throws IOException;

    void autoPdfParseToAbbsAndHdProcessDetailSyn(String redisKey, PDFRenderer pdfRenderer, int pageCounter) throws IOException;


}
