package com.wuweibi.bullet.domain2.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DomainCertUpdate {


    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty("证书私钥")
    @NotBlank(message = "证书私钥不能为空")
    @Length(message = "证书私钥不能超过个 {max} 字符", max = 4000)
    private String certKey;

    @ApiModelProperty("PEM证书")
    @NotBlank(message = "PEM证书不能为空")
    @Length(message = "PEM证书不能超过个 {max} 字符", max = 4000)
    private String certPem;

}
