package com.ruoguang.dcs.service.impl;

import cn.hutool.core.date.DateUtil;

import com.ruoguang.dcs.util.FilesUtils;
import com.ruoguang.dcs.util.PathUtils;
import com.ruoguang.dcs.util.UuidUtils;
import com.ruoguang.dcs.util.html2excle.utils.TableToXls;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;

@Service
public class Html2ExcelService {
    /**
     * 解析生成markdown
     *
     * @param pageUrl
     * @return
     */
    public String excute(String pageUrl) throws Exception {
        String outputPath = new StringBuffer("/output/").append(DateUtil.format(new Date(),"yyyyMMdd")).append("/excel/").append(UuidUtils.uuid32()).append(".xls").toString();
        URL url = new URL(pageUrl);
        String absoultOutputPath = PathUtils.getClassRootPath(outputPath);
        FilesUtils.checkFolderAndCreate(absoultOutputPath);
        FileOutputStream fout = new FileOutputStream(absoultOutputPath);
        TableToXls.process(url, 30000, fout);
        fout.close();
        return outputPath;
    }
}