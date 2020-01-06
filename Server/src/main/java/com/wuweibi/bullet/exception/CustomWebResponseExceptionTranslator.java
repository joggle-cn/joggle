package com.wuweibi.bullet.exception;

import com.wuweibi.bullet.entity.api.Result;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) {

        if(e instanceof OAuth2Exception){
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(200)
                    .body("dsadsad");
        } else if(e instanceof InternalAuthenticationServiceException){
            InternalAuthenticationServiceException ex = (InternalAuthenticationServiceException)e;

            OAuth2Exception oAuth2Exception =  (OAuth2Exception) ex.getCause();

            return ResponseEntity
                    .status(200)
                    .body("dsadsaddsadsad");

        } else if(e instanceof InsufficientAuthenticationException) {
            return ResponseEntity
                    .status(200).body(Result.fail(AuthErrorType.INVALID_LOGIN));
        } else {
            return ResponseEntity
                    .status(500).body(null);
        }
    }
}
