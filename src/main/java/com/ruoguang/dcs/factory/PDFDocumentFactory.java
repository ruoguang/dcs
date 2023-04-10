package com.ruoguang.dcs.factory;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.pdfbox.pdmodel.PDDocument;
@Setter
@Getter
public class PDFDocumentFactory extends BasePooledObjectFactory<PDDocument> {

    private byte[] bytes;

    public PDFDocumentFactory(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public PDDocument create() throws Exception {
        PDDocument document = PDDocument.load(bytes);
        return document;
    }

    @Override
    public void destroyObject(PooledObject<PDDocument> p) throws Exception {
        PDDocument document = p.getObject();
        if (document != null) {
            document.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<PDDocument> p) {
        PDDocument document = p.getObject();
        return document != null;
    }

    @Override
    public PooledObject<PDDocument> wrap(PDDocument document) {
        return new DefaultPooledObject<>(document);
    }

}
