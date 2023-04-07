package com.ruoguang.dcs.listener;

import java.io.File;

/**
 * 网络文件下载监听器
 */
public interface IOnDownloadListener {
    /**
     * @param file 下载成功后的文件，这里可以对这个文件做操作
     */
    void onDownloadSuccess(File file);

    /**
     * @param fileName 文件名
     * @param progress 下载进度
     */
    void onDownloading(String fileName, int progress);

    /**
     * @param e 下载异常信息
     */
    void onDownloadFailed(Exception e);
}