package com.ruoguang.dcs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoguang.dcs.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * html转pdf的服务
 *
 * author cc cc 欧阳洁
 * @since 2018-03-28 11:50
 */
@Service
public class Html2PdfService {

    @Autowired
    private OsExeUtils osExeUtils;

    /**
     * 解析生成PDF
     *
     * @param pageUrl
     * @return
     */
    public String excute(String pageUrl) throws Exception {
        String outputPath = new StringBuffer("/output/").append(DateUtil.format(new Date(), "yyyyMMdd")).append("/pdf/").append(UuidUtils.uuid32()).append(".pdf").toString();
        String cmdStr = getCmdStr(pageUrl, outputPath);
        boolean success = CmdUtils.excute(cmdStr);
        if (success) {
            return outputPath;
        } else {
            if (FilesUtils.isExistNotCreate(outputPath)) {
                return outputPath;
            } else {
                throw new Exception("转化异常！[" + outputPath + "]");
            }
        }
    }

    /**
     * 根据操作系统类别，获取不同的cmd字符串
     *
     * @param pageUrl
     * @param outputPath
     * @return
     */
    private String getCmdStr(String pageUrl, String outputPath) {
        StringBuilder cmdStr = new StringBuilder();
        String absoultOutputPath = PathUtils.getClassRootPath(outputPath);
        /************************输出文件夹检查************************/
        FilesUtils.checkFolderAndCreate(absoultOutputPath);
        String absoultExePath = "";
        if (OsInfo.isWindows()) {//windows系统
            absoultExePath = osExeUtils.getWindowExePath();
            absoultOutputPath = PathUtils.getWindowsRightPath(absoultOutputPath);
        } else {//默认linux系统
            absoultExePath = osExeUtils.getLinuxExePath();
            //需要给脚本授权
            //cmdStr.append("chmod +x ").append(absoultExePath).append(" && ");
            CmdUtils.excute("chmod +x " + absoultExePath);
        }
        cmdStr.append(absoultExePath).append(" ").append("--encoding utf-8").append(" ").append(pageUrl).append(" ").append(absoultOutputPath);
        return cmdStr.toString();
    }


}
