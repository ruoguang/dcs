package com.ruoguang.dcs.service;

import javax.servlet.http.HttpServletResponse;

public interface IOpenClientService {

    String viewUrlForword(String url, HttpServletResponse response) throws Exception;
}
