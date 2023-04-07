package com.ruoguang.dcs.service.impl;


import com.aspose.pdf.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.ruoguang.dcs.service.IWord2PdfService;
import javassist.ClassPool;
import javassist.CtClass;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;


@Service("asposeWordService")
public class AsposeWordServiceImpl implements IWord2PdfService {

    @Override
    public void word2pdf(InputStream inputStream, OutputStream outputStream) throws Exception {
        if (getLicense()) {
            Document doc = new Document(inputStream);
            doc.save(outputStream,SaveFormat.PDF);
        }
    }


    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = AsposeWordServiceImpl.class.getClassLoader().getResourceAsStream("license.xml");
            License license = new License();
            license.setLicense(is);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void pdf2word(InputStream inputStream, OutputStream outputStream) throws Exception {
        if (getLicense()) {
            com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(inputStream);
            // 初始化doc保存选项
            DocSaveOptions options = new DocSaveOptions();
            // 设置输出格式为doc
            options.setFormat(DocSaveOptions.DocFormat.Doc);
            // 将pdf文档保存为doc格式
            pdfDocument.save(outputStream, options);
        }
    }
}