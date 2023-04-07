package com.ruoguang.dcs.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface IWord2PdfService {

    void word2pdf(InputStream inputStream, OutputStream outputStream) throws Exception;

    void pdf2word(InputStream inputStream, OutputStream outputStream) throws Exception;



}