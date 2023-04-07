package com.ruoguang.dcs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoguang.dcs.util.FilesUtils;
import com.ruoguang.dcs.util.HTML2Md;
import com.ruoguang.dcs.util.PathUtils;
import com.ruoguang.dcs.util.UuidUtils;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class Html2MarkdownService {
    /**
     * 解析生成markdown
     *
     * @param pageUrl
     * @return
     */
    public String excute(String pageUrl) throws Exception {
        String outputPath = new StringBuffer("/output/").append(DateUtil.format(new Date(),"yyyyMMdd")).append("/markdown/").append(UuidUtils.uuid32()).append(".md").toString();
        URL url = new URL(pageUrl);
        String markdownStr = HTML2Md.convert(url, 30000);
        String absoultOutputPath = PathUtils.getClassRootPath(outputPath);
        FilesUtils.checkFolderAndCreate(absoultOutputPath);
        FilesUtils.newFile(absoultOutputPath,markdownStr,"UTF-8");
        return outputPath;
    }
}