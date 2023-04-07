package com.ruoguang.dcs.util;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoguang.dcs.listener.IOnDownloadListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("all")
public class OkHttpClientUtil {
    //shared perform best
    private static final OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MULTIPART_TYPE = MediaType.parse("multipart/form-data");

    public static Response ajaxURL(String url, String method, Headers headers, JSONObject requestData) {
        try {
            Request.Builder requestBuilder = new Request
                    .Builder()
                    .url(url)
                    .headers(headers);

            if (Objects.equals(method.toUpperCase(), "GET")) {
                requestBuilder.get();
            } else {
                RequestBody requestBody = RequestBody.create(JSON_TYPE, requestData == null ? "" : objectMapper.writeValueAsString(requestData));

                requestBuilder.method(method.toUpperCase(), requestBody);
            }
            Response response = okHttpClient.newCall(requestBuilder.build()).execute();

            log.info("response->{}", response);
            return response;
        } catch (Exception e) {
            log.error("Exception e->{}", e);
        }
        return null;
    }

    /**
     * @param host
     * @param uri
     * @param method
     * @param headers
     * @param requestData
     * @return
     */
    public static Response ajax(String host, String uri, String method, Headers headers, JSONObject requestData) {
        log.info("host->{}", host);
        log.info("uri->{}", uri);
        log.info("method->{}", method);
        log.info("headers->{}", headers);
        log.info("requestData->{}", requestData);
        String urlWithQueryParam = String.format("%s%s", host, uri);
        return ajaxURL(urlWithQueryParam, method, headers, requestData);
    }

    /**
     * @param host
     * @param uri
     * @param method
     * @param headers
     * @param requestData
     * @return
     */
    public static Response update(String host, String uri, Headers headers, JSONObject requestData, String parameterName, File file) {
        log.info("host->{}", host);
        log.info("uri->{}", uri);
        log.info("headers->{}", headers);
        log.info("requestData->{}", requestData);
        String urlWithQueryParam = String.format("%s%s", host, uri);

        try {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

            MultipartBody body = new MultipartBody.Builder()
                    .setType(MULTIPART_TYPE)
                    .addFormDataPart(parameterName, file.getName(), fileBody)
                    .build();

            Request.Builder requestBuilder = new Request
                    .Builder()
                    .post(body)
                    .url(urlWithQueryParam)
                    .headers(headers);

            Response response = okHttpClient.newCall(requestBuilder.build()).execute();
            log.info("response->{}", response);
            return response;
        } catch (Exception e) {
            log.error("Exception e->{}", e);
        }
        return null;
    }


    /**
     * 同步下载网络文件
     *
     * @param url          下载的链接，精确到文件
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称（自己命名）
     * @return file
     */
    public static File downloadSyn(final String url, final String destFileDir, final String destFileName) {
        Request request = new Request.Builder().url(url).build();
        // 同步请求
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // 储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);
                is = response.body().byteStream();
                fos = new FileOutputStream(file);
                int size = 0;
                long total = response.body().contentLength();
                while ((size = is.read(buf)) != -1) {
                    len += size;
                    fos.write(buf, 0, size);
                    int process = (int) Math.floor(((double) len / total) * 100);
                    log.info("FileName：{}，Downloading progress：{} %", destFileName, process);
                }
                fos.flush();
                return file;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            log.error("error:{}", e);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 异步下载网络文件
     *
     * @param url          下载的链接，精确到文件
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称（自己命名）
     * @param listener     下载监听
     */
    public static void downloadsASyn(final String url, final String destFileDir, final String destFileName, final IOnDownloadListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;

                // 储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);

                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    int size = 0;
                    long total = response.body().contentLength();
                    while ((size = is.read(buf)) != -1) {
                        len += size;
                        fos.write(buf, 0, size);
                        int process = (int) Math.floor(((double) len / total) * 100);
                        // 控制台打印文件下载的百分比情况
                        listener.onDownloading(destFileName, process);
                    }

                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    log.error("error:{}", e);
                    listener.onDownloadFailed(e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }


}
