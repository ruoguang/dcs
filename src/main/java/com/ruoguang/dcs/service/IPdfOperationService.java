package com.ruoguang.dcs.service;


import com.ruoguang.dcs.pojo.qo.PdfMergeQO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IPdfOperationService {
    /**
     * 顺序合并
     *
     * @param inputStreams 输入流集合
     * @return 输出流
     * @throws IOException io异常
     */
    String order2Merge(List<InputStream> inputStreams, String outFileName) throws IOException;

    String order2MergeResult(PdfMergeQO pdfMergeQO, HttpServletResponse response) throws Exception;

}
