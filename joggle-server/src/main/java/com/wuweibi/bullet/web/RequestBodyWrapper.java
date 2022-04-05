package com.wuweibi.bullet.web;

import com.wuweibi.bullet.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;


/**
 * 问题：使用RequestWrapper类后，造成了部分post请求，controller无法获取到入参而导致项目部分功能不可用
 * 原因：InputStream只可读取一次。  由于RequestWrapper里读取了InputStream，导致request的流已空，
 * 所以controller里面getParameter无法获取到内容
 * 解决方案：根据ContentType类型做不同处理
 */
public class RequestBodyWrapper extends HttpServletRequestWrapper {

    private Logger logger = LoggerFactory.getLogger(RequestBodyWrapper.class);

    public static final String FORM_CONTENT_TYPE = "x-www-form-urlencoded";
    public static final String FORM_DATA_CONTENT_TYPE = "multipart/form-data";


    private ByteArrayInputStream cacheInputStream;

    public RequestBodyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String sessionStream = getBodyString(request);
        byte[] body = sessionStream.getBytes(Charset.forName("UTF-8"));
        cacheInputStream = new ByteArrayInputStream(body);
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public String getBodyString(final ServletRequest request) {
        String contentType = request.getContentType();
        StringBuilder bodyString = new StringBuilder();
        if (StringUtil.isNotBlank(contentType) &&
                (contentType.contains(FORM_DATA_CONTENT_TYPE) || contentType.contains(FORM_CONTENT_TYPE))) {
            Enumeration<String> pars = request.getParameterNames();
            while (pars.hasMoreElements()) {
                String n = pars.nextElement();
                bodyString.append(n).append("=").append(request.getParameter(n));
                if (pars.hasMoreElements()) {
                    bodyString.append("&");
                }
            }
            return bodyString.toString();
        }

        try {
            byte[] byteArray = StreamUtils.copyToByteArray(request.getInputStream());
            return new String(byteArray, "UTF-8");
        } catch (IOException e) {
            logger.error("", e);
        }

        return bodyString.toString();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        cacheInputStream.reset();
        return new ServletInputStream() {

            @Override
            public int read() {
                return cacheInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }


}
