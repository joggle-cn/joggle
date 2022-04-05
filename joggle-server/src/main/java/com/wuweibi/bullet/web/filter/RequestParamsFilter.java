package com.wuweibi.bullet.web.filter;

import com.wuweibi.bullet.utils.StringUtil;
import com.wuweibi.bullet.web.RequestBodyWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 请求参数过滤器
 * 输出请求参数，支持form表单以及body json
 *
 * @author marker
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestParamsFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(RequestParamsFilter.class);


    public static final String FORM_CONTENT_TYPE = "x-www-form-urlencoded";
    public static final String FORM_DATA_CONTENT_TYPE = "multipart/form-data";

    private static PathMatcher pathMatcher = new AntPathMatcher();


    private static final String[] bodyRequestMethods = new String[]{HttpMethod.POST.name(),
            HttpMethod.DELETE.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name()};


    private static final String[] IGNORE_URLS = new String[]{
            "/actuator",
            "/actuator/**",
            "/resource/**",
            "/lib/**",
            "/js/**",
            "/css/**",
            "/template/**",
    };




    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestBodyWrapper request = new RequestBodyWrapper(httpServletRequest);

        String uri = request.getRequestURI();
        if (isNoLogger(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("============== Request Info Start ================");
        String method = request.getMethod();
        String params = request.getQueryString();
        logger.info("{} {}{}", method, uri, params == null ? "" : "?" + params);
        if (ArrayUtils.contains(bodyRequestMethods, method)) {
            if (isFormRequest(request)) {
                logger.info("form:{}", getBody(request));
            } else {
                logger.info("body:{}", getBody(request));
            }
        }
        logger.info("============== Request Info End  =============");

        filterChain.doFilter(request, response);
    }



    private boolean isNoLogger(String uri) {
        for (String ignoreUrl : IGNORE_URLS) {
            return pathMatcher.match(ignoreUrl, uri);
        }
        return false;
    }


    /**
     * 判断是否是Form请求
     *
     * @param request
     * @return
     */
    private boolean isFormRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType !=null && (contentType.contains(FORM_CONTENT_TYPE) ||
                contentType.contains(FORM_DATA_CONTENT_TYPE));
    }

    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        String method = request.getMethod();
        // 获取请求体信息
        if (ArrayUtils.contains(bodyRequestMethods, method)) {
            String formatted = formatValue(
                    StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset()), 1024);
            body.append(formatted);
        }
        return  StringUtil.removeAllLineBreaks(body.toString());
    }



    /**
     * Format the given value via {@code toString()}, quoting it if it is a
     * {@link CharSequence}, and possibly truncating at 100 if limitLength is
     * set to true.
     * @param value the value to format
     * @param limitLength whether to truncate large formatted values (over 100)
     * @return the formatted value
     */
    public static String formatValue(Object value, Integer limitLength) {
        if (value == null) {
            return "";
        }
        String str;
        if (value instanceof CharSequence) {
            str = "\"" + value + "\"";
        }
        else {
            try {
                str = value.toString();
            }
            catch (Throwable ex) {
                str = ex.toString();
            }
        }
        return (limitLength != null && str.length() > limitLength ? str.substring(0, limitLength) + "..." : str);
    }

}
