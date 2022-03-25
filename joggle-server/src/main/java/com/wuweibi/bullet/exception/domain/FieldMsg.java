package com.wuweibi.bullet.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 字段错误信息类，用于描述多个字段的错误信息
 *
 * @author marker
 */
@Data
@AllArgsConstructor
public class FieldMsg {

    /**
     * 字段名称
     */
    private String field;

    /**
     * 字段错误信息
     */
    private String msg;
}
