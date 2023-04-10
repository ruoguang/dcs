package com.ruoguang.dcs.pool;

import com.ruoguang.dcs.factory.PDFDocumentFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.IOException;

public class PDFDocumentPool {

    private static GenericObjectPool<PDDocument> objectPool;

    static {
        // 配置对象池
        GenericObjectPoolConfig<PDDocument> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10); // 最大对象数
        config.setMaxIdle(5); // 最大空闲对象数
        config.setMinIdle(2); // 最小空闲对象数

        // 创建对象池
        byte[] bytes = new byte[0];
        objectPool = new GenericObjectPool<>(new PDFDocumentFactory(bytes), config);
    }

    /**
     * 借出对象
     *
     * @return
     * @throws Exception
     */
    public static PDDocument borrowObject(byte[] bytes) throws Exception {
        PDFDocumentFactory factory = (PDFDocumentFactory) objectPool.getFactory();
        factory.setBytes(bytes);
        return objectPool.borrowObject();
    }

    /**
     * 归还对象
     *
     * @param document
     * @throws Exception
     */
    public static void returnObject(PDDocument document) throws IOException {
        document.close();
        objectPool.returnObject(document);
    }

}

