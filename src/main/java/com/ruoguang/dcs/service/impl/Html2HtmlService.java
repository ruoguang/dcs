package com.ruoguang.dcs.service.impl;


import cn.hutool.core.date.DateUtil;
import com.ruoguang.dcs.util.FilesUtils;
import com.ruoguang.dcs.util.PathUtils;
import com.ruoguang.dcs.util.UuidUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Html2HtmlService {
    /**
     * 转储HTML字符串为HTML页面
     *
     * @param pageHtmlContent
     * @return
     */
    public String excute(String pageHtmlContent) throws Exception {
        String outputPath = new StringBuffer("/output/").append(DateUtil.format(new Date(), "yyyyMMdd")).append("/html/").append(UuidUtils.uuid32()).append(".html").toString();
        String absoultOutputPath = PathUtils.getClassRootPath(outputPath);
        FilesUtils.checkFolderAndCreate(absoultOutputPath);
        FilesUtils.newFile(absoultOutputPath, pageHtmlContent, "UTF-8");
        return outputPath;
    }
}