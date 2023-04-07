package com.ruoguang.dcs.util;




import cn.hutool.core.io.FileUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoguang.dcs.model.AjaxResult;
import com.ruoguang.dcs.pojo.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 开放发送请求 通用
 */
@Component
@Slf4j
public class OpenClient {


    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final ObjectMapper objectMapper = new ObjectMapper();
    //shared perform best
    private final OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build();




    /**
     * @param uriWithParam 请求的URL和参数
     * @param method       HTTP请求动词
     * @param requestData  请求的内容
     * @return JSON字符串
     */
    public AjaxResult executeRequest(String uriWithParam, String method, JSONObject requestData, Long timestamp, String uploadpath) {
        if (StringUtils.isNoneBlank(uploadpath)) {
            String absoultExePath = PathUtils.getClassRootPath(uploadpath);
            if (OsInfo.isWindows()) {
                uploadpath = PathUtils.getWindowsRightPath(absoultExePath);
            } else {
                uploadpath = PathUtils.getLinuxRightPath(absoultExePath);
            }
            FileUtil.mkdir(uploadpath);
        }
        return executeRequest(uriWithParam, method, requestData, 1, timestamp, uploadpath);
    }

    private AjaxResult executeRequest(String uriWithParam, String method, JSONObject requestData, int retryTime, Long timestamp, String uploadpath) {
        log.info(" ********** 开放请求 start **********");
        log.info(" ********** 开放请求 uri ->{} **********", uriWithParam);
        log.info(" ********** 开放请求 method ->{} **********", method);
        log.info(" ********** 开放请求 requestData ->{} **********", requestData);
        log.info(" ********** 开放请求 retryTime ->{} **********", retryTime);
        log.info(" ********** 开放请求 timestamp ->{} **********", timestamp);
        log.info(" ********** 开放请求 uploadpath ->{} **********", uploadpath);

        ResponseVO responseVO = new ResponseVO();
        AjaxResult result = AjaxResult.success();

        try {
            String urlWithQueryParam = String.format("%s", uriWithParam);
            Headers headers = new Headers
                    .Builder()
                    .build();


            final Request.Builder requestBuilder = new Request
                    .Builder()
                    .url(urlWithQueryParam)
                    .headers(headers);

            if (Objects.equals(method.toUpperCase(), "GET")) {
                requestBuilder.get();
            } else {

                final RequestBody requestBody = RequestBody.create(JSON_TYPE, requestData == null ? "" : objectMapper.writeValueAsString(requestData));
                requestBuilder.method(method.toUpperCase(), requestBody);
            }

            final Response response = okHttpClient.newCall(requestBuilder.build()).execute();

            if (response.code() == 401) {
                // token失效, 重新获取token
//                    invalidToken(token);
                String re = response.body().string();
                log.info("请求开放接口401，错误信息：[{}]", re);
                return AjaxResult.error("请检查该开放者和该私钥是否匹配，接口返回为401！\n" + re);
            }
            //判断返回是否为200
            else if (response.code() == 200) {
                //判断返回是否为下载的zip，合同下载为zip
                if (response.headers().get("Content-Type").equals("application/zip")) {
                    String Mypath = "";
                    //判断请求头中是否有下载标识

                        responseVO.setReposebody(response.body() != null ? response.body().string() : null);
                        result.setData(responseVO);
                        log.info("返回参数：Token:[{}],MD5:[{}],SignString:[{}],SignData:[{}],Sign:[{}],ReposeBody:[{}]",
                                responseVO.getToken(), responseVO.getMd5(), responseVO.getSignString(),
                                responseVO.getSignData(), responseVO.getSign(), (isjson(responseVO.getReposebody())) ? responseVO.getReposebody() : "下载合同成功");
                        return result;


                }
                //判断下载是否为pdf，附页是会有这种标识
                if (response.headers().get("Content-Type").equals("application/pdf")) {
                    byte[] result2 = response.body().bytes();
                    responseVO.setReposebody(Base64Util.byteArrToBase64(result2));
                    result.setData(responseVO);
                    return result;
                }
                String a = response.body().string();
                JSONObject userObj = JSON.parseObject(a);




                if (userObj.get("data") instanceof JSONArray) {
                    System.out.println("返回的是个数组，值为：");
                    System.out.println(userObj.get("data").toString());
                } else if (userObj.get("data") instanceof JSONObject) {
                    System.out.println("返回的是个json，值为：");
                    System.out.println(userObj.get("data").toString());
                }




                responseVO.setReposebody(userObj != null ? a : null);
                result.setData(responseVO);

                System.out.println(response.headers().get("Content-Type"));

                log.info("返回参数：Token:[{}],MD5:[{}],SignString:[{}],SignData:[{}],Sign:[{}],ReposeBody:[{}]",
                        responseVO.getToken(), responseVO.getMd5(), responseVO.getSignString(),
                        responseVO.getSignData(), responseVO.getSign(), (isjson(responseVO.getReposebody())) ? responseVO.getReposebody() : "下载成功");

                return result;
            } else if (response.code() == 404) {
                String re = response.body().string();
                log.info("请求开放接口404，错误信息：[{}]", re);
                return AjaxResult.error("请检查方法名是否正确，接口返回为404！\n" + re);
            } else {
                String re = response.body().string();
                log.info("请求开放接口出错，错误信息：[{}]", re);
                return AjaxResult.error(re);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("出现未知错误！[{}]", ex.getMessage());
            return AjaxResult.error("出现未知错误，请检查！" + ex);
        }
    }


    /**
     * downloadPath未存储的路径，路径包括了服务器存储路径，以及文件名称，比如：d:/test.zip
     *
     * @param filepath
     * @param data
     * @throws Exception
     */
    public static void saveFile(String filepath, byte[] data) throws Exception {
        if (data != null) {
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.close();
        }
    }

    private boolean isjson(String string) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void base64StringToPDF(String base64String, File file) {
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        try {
            //将base64编码的字符串解码成字节数组
            byte[] bytes = Base64.getDecoder().decode(base64String);
            //创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            //创建从底层输入流中读取数据的缓冲输入流对象
            bin = new BufferedInputStream(bais);
            //创建到指定文件的输出流
            fout = new FileOutputStream(file);
            //为文件输出流对接缓冲输出流对象
            bout = new BufferedOutputStream(fout);

            byte[] buffers = new byte[1024];
            int len = bin.read(buffers);
            while (len != -1) {
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bout.close();
                fout.close();
                bin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code 加密的base64
     * @param targetPath 保存的文件夹路径名
     */
    public static void decoderBase64File(String base64Code, String targetPath) {
        try {
            byte[] buffer = Base64.getDecoder().decode(base64Code);
            FileOutputStream out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //base64解密zip，并且解压缩
    public static void Base64ToFile(String base64, String path) throws RuntimeException {

        try {
            byte[] byteBase64 = Base64.getDecoder().decode(base64);
            ByteArrayInputStream byteArray = new ByteArrayInputStream(byteBase64);
            ZipInputStream zipInput = new ZipInputStream(byteArray);
            ZipEntry entry = zipInput.getNextEntry();
            File fout = null;
            File file = new File(path);
            String parent = file.getParent();
            while (entry != null && !entry.isDirectory()) {

                fout = new File(parent, entry.getName());
                if (!fout.exists()) {
                    (new File(fout.getParent())).mkdirs();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fout));
                int offo = -1;
                byte[] buffer = new byte[1024];
                while ((offo = zipInput.read(buffer)) != -1) {
                    bos.write(buffer, 0, offo);
                }
                bos.close();
                // 获取 下一个文件
                entry = zipInput.getNextEntry();
            }
            zipInput.close();
            byteArray.close();
        } catch (Exception e) {
            throw new RuntimeException("解压流出现异常", e);
        }

    }


}
