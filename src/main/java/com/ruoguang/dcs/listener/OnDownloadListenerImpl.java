package com.ruoguang.dcs.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class OnDownloadListenerImpl implements IOnDownloadListener {
    @Override
    public void onDownloadSuccess(File file) {
        log.info("File Download success,FileName:{}", file.getName());
    }

    @Override
    public void onDownloading(String fileName, int progress) {
        log.info("FileName：{}，Downloading progress：{} %", fileName, progress);
    }

    @Override
    public void onDownloadFailed(Exception e) {
        log.error("File Download Exception->{}", e);
    }
}
