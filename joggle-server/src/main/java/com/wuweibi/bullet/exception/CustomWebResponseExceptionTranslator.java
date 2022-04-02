package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;


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
    public ResponseEntity translate(Exception e) {


        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        Exception ase = (AuthenticationException) throwableAnalyzer
                .getFirstThrowableOfType(AuthenticationException.class, causeChain);




        if (e instanceof OAuth2Exception) {
            log.warn("", e);
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
            log.warn("", e);
            return ResponseEntity
                    .status(200).body(R.fail(AuthErrorType.INVALID_LOGIN));
        } else {
            return ResponseEntity
                    .status(500).body(null);
        }
    }
}
