package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.oauth2.exception.MyAuth2Exception;
import com.wuweibi.bullet.ratelimiter.util.WebUtils;
import com.wuweibi.bullet.utils.SpringUtils;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;

import java.nio.charset.StandardCharsets;


/**
 * 异常翻译
 *
 * @author marker
 */
@Slf4j
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {



    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

//    protected MessageSourceAccessor messages = SecurityMessageSourceUtil.getAccessor();




    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        Exception ase = (AuthenticationException) throwableAnalyzer
                .getFirstThrowableOfType(AuthenticationException.class, causeChain);

        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            if (oAuth2Exception instanceof InvalidGrantException) {
                if ("User is disabled".equals(oAuth2Exception.getMessage())) {
                    return ResponseEntity
                            .status(200).body(R.fail(AuthErrorType.ACCOUNT_NOT_ACTIVATE));
                }
                return ResponseEntity
                        .status(200).body(R.fail(AuthErrorType.ACCOUNT_PASSWORD_ERROR));
            }
            return ResponseEntity
                    .status(200).body(R.fail(oAuth2Exception));

        } else if (e instanceof InternalAuthenticationServiceException) {
            InternalAuthenticationServiceException ex = (InternalAuthenticationServiceException) e;
            String msg = "";
            if (ex.getCause() instanceof BaseException) {
                Exception exception = (Exception) ex.getCause();
                msg = exception.getMessage();
            } else {
                OAuth2Exception oAuth2Exception = (OAuth2Exception) ex.getCause();
                msg = oAuth2Exception.getMessage();
            }
            return ResponseEntity
                    .status(200).body(R.fail(SystemErrorType.newErrorType(msg)));

        } else if (e instanceof InsufficientAuthenticationException) {
            AdminServerProperties adminServerProperties = SpringUtils.getBean(AdminServerProperties.class);
            if (WebUtils.getRequest().getRequestURI().startsWith(adminServerProperties.getContextPath())) { // 监控
                throw e;
            }
            log.warn("", e);
            return ResponseEntity
                    .status(200).body(R.fail(AuthErrorType.INVALID_LOGIN));
        }


        // RException 处理
        ase = (RException) throwableAnalyzer.getFirstThrowableOfType(RException.class,
                causeChain);
        if (ase != null) {//
            return handleRException((RException)ase);
        }

        return ResponseEntity
                .status(500).body(null);
    }





    private ResponseEntity<OAuth2Exception> handleRException(RException e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
//		if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
//			headers.set(HttpHeaders.WWW_AUTHENTICATE,
//					String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
//		}
        MyAuth2Exception exception = new MyAuth2Exception(e.getResult());
        // 客户端异常直
        return new ResponseEntity<>(exception, headers,
                HttpStatus.valueOf(200));

    }
}
