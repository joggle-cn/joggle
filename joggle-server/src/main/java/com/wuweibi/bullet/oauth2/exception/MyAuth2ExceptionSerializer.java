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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import lombok.SneakyThrows;

/**
 * @author system
 * @date 2020/11/16
 * <p>
 * OAuth2 异常格式化
 */
public class MyAuth2ExceptionSerializer extends StdSerializer<MyAuth2Exception> {

    public MyAuth2ExceptionSerializer() {
        super(MyAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(MyAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        R<Object> r = new R(SystemErrorType.SYSTEM_ERROR, value.getMessage(), null);
        if (value.getResult() != null) {
            r.setCode(value.getResult().getCode());
            r.setData(value.getResult().getData());
        }
        gen.writeObject(r);
        gen.flush();
        gen.close();
    }

}
