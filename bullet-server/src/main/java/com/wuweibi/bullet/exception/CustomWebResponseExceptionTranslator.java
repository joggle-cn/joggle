package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;


/**
 * 异常翻译
 * @author marker
 */
@Slf4j
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) {

        if(e instanceof OAuth2Exception){
            log.warn("", e);
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            if(oAuth2Exception instanceof InvalidGrantException){
                return ResponseEntity
                        .status(200).body(Result.fail(AuthErrorType.ACCOUNT_PASSWORD_ERROR));
            }
            return ResponseEntity
                    .status(200).body(Result.fail(oAuth2Exception));

        } else if(e instanceof InternalAuthenticationServiceException){
            InternalAuthenticationServiceException ex = (InternalAuthenticationServiceException)e;

            OAuth2Exception oAuth2Exception =  (OAuth2Exception) ex.getCause();

            return ResponseEntity
                    .status(200)
                    .body("dsadsaddsadsad");

        } else if(e instanceof InsufficientAuthenticationException) {
            log.warn("", e);
            return ResponseEntity
                    .status(200).body(Result.fail(AuthErrorType.INVALID_LOGIN));
        } else {
            return ResponseEntity
                    .status(500).body(null);
        }
    }
}
