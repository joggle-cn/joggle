package com.wuweibi.bullet.system.client.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *  程序版本vo
 * </p>
 *
 * @author marker
 * @since 2024-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NgrokVersionVO implements Serializable {


    @Schema(description = "ngrok客户端版本 v1.3.9")
    private String clientVersion;


    @Schema(description = "ngrok服务端版本 v1.3.9")
    private String serverVersion;



}
