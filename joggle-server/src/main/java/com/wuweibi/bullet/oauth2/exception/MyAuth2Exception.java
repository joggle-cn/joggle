/*
 *    Copyright (c) 2018-2025, jiayu All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the jiayu.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: jiayu
 */

package com.wuweibi.bullet.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuweibi.bullet.entity.api.R;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author system
 * @date 2020/7/8 自定义OAuth2Exception
 */ @Getter
@JsonSerialize(using = MyAuth2ExceptionSerializer.class)
public class MyAuth2Exception extends OAuth2Exception {

	@Getter
	private String errorCode;


	private R result;

	public MyAuth2Exception(String msg) {
		super(msg);
	}

	public MyAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public MyAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

	public MyAuth2Exception(R result) {
		super(result.getMsg());
		this.result = result;
	}

}
