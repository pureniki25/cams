package com.hongte.alms.open.util;


import com.alibaba.fastjson.JSON;
import com.hongte.alms.common.open.OpenJsonConvert;
import com.hongte.alms.common.open.OpenRequestContent;
import com.hongte.alms.common.open.OpenSecureUtil;
import com.hongte.alms.open.config.OpenServiceConfig;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;

@ControllerAdvice(basePackages = "com.hongte.alms.open")
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {
    //增加日志
    private final Logger logger = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);

    @Autowired
    private OpenServiceConfig openServiceConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getMethodAnnotation(TripleDESDecrypt.class) != null;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage request, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage request, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String inputStr = IOUtils.toString(request.getBody(), "UTF-8");
        OpenRequestContent requestContent = JSON.parseObject(inputStr, OpenRequestContent.class);
        String decodedText = URLDecoder.decode(requestContent.getStrJson(), "UTF-8");
        String decryptedText = null;
        try {
            decryptedText = OpenSecureUtil.tripleDESDecrypt(openServiceConfig.getTripleDESKey(), decodedText);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return new DecodedHttpInputMessage(request.getHeaders(), new ByteArrayInputStream(decryptedText.getBytes("UTF-8")));

    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    static class DecodedHttpInputMessage implements HttpInputMessage {
        HttpHeaders headers;
        InputStream body;

        public DecodedHttpInputMessage(HttpHeaders headers, InputStream body) {
            this.headers = headers;
            this.body = body;
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
