package com.wuweibi.bullet.domain2.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class DomainCertUpdate {


    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @Schema(description = "证书私钥")
    @NotBlank(message = "证书私钥不能为空")
    @Length(message = "证书私钥不能超过个 {max} 字符", max = 4000)
    private String certKey;

    @Schema(description = "PEM证书")
    @Length(message = "PEM证书不能超过个 {max} 字符", max = 4000)
    private String certPem;


    @Schema(description = "是否自动续期 1自动 0关闭")
    private Boolean isAutoRenewal;

}
