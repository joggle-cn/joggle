

package com.wuweibi.bullet.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.ErrorType;

import java.util.Map;

/**
 *
 *
 * @author system
 * @date 2020/7/8
 */
@JsonSerialize(using = MyAuth2ExceptionSerializer.class)
public class LoginException extends MyAuth2Exception {

	public LoginException(ErrorType errorCode, Map<String, Object> data) {
		super(R.fail(errorCode) );
	}

	public LoginException(String msg) {
		super(R.fail(msg) );
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "loginfaild_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 200;
	}

}
