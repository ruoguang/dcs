package com.ruoguang.dcs.util;




import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class Base64Util {

    /**
     * 将文件转成base64 字符串
     * 不含/r/n
     *
     * @param file 文件
     * @return *
     * @throws Exception
     */
    public static String file2base64Str(File file) throws Exception {

        byte[] bFile;
        byte[] bEncodedFile = null;
        try {
            bFile = Files.readAllBytes(file.toPath());
            bEncodedFile = Base64.getEncoder().encode(bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bEncodedFile);
    }


    /**
     * 将base64字符解码保存文件
     * 不含/r/n
     *
     * @param base64Code
     * @param file
     * @throws Exception
     */
    public static void base64Str2file(String base64Code, File file) throws Exception {
        FileOutputStream fos;
        byte[] bFileDecodeString = Base64.getDecoder().decode(base64Code);
        try {
            fos = new FileOutputStream(file);
            fos.write(bFileDecodeString);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * base64TobyteArr
     *
     * @param base64code
     * @return
     */
    public static byte[] base64ToByteArr(String base64code) {
        return DatatypeConverter.parseBase64Binary(base64code);
    }

    /**
     * byteArrTobyteArr
     *
     * @param bytes
     * @return
     */
    public static String byteArrToBase64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }


    /**
     * BufferedImage 编码转换为 base64
     *
     * @param bufferedImage
     * @return
     */
    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return byteArrToBase64(bytes);
    }

    /**
     * base64 编码转换为 BufferedImage
     *
     * @param base64
     * @return
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64转inputStream流
     *
     * @param base64str
     * @return
     */
    public static InputStream base64ToInputStream(String base64str) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = base64ToByteArr(base64str);
            stream = new ByteArrayInputStream(bytes);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }


}
