package com.ruoguang.dcs.advice;


import com.ruoguang.dcs.model.ReturnInfo;
import com.ruoguang.dcs.util.ReturnInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

/**
 * RestReturnValueHandlerAdvice
 *
 * @author cc
 * @date 2020/06/04
 */
@Slf4j

public class RestReturnValueHandlerAdvice implements HandlerMethodReturnValueHandler {
    private HandlerMethodReturnValueHandler proxyObject;

    public RestReturnValueHandlerAdvice(HandlerMethodReturnValueHandler proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return proxyObject.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        if (response != null) {
            String contentType = response.getHeader("content-type");
            if (contentType == null || contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                ReturnInfo<?> result = ReturnInfoUtil.success(returnValue);
                log.info("ReturnInfo result :{}", result);
                proxyObject.handleReturnValue(result, returnType, mavContainer, webRequest);
            } else {
                proxyObject.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            }
        } else {
            proxyObject.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }
    }
}
