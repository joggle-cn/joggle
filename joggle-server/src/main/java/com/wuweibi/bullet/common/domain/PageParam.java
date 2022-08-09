package com.wuweibi.bullet.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页参数")
public class PageParam {


    @ApiModelProperty(value = "页码 默认 1")
    private Integer current = 1;

    @ApiModelProperty(value = "条数 默认10")
    private Integer size = 10;

    public PageParam(){}

    public PageParam(Integer current, Integer size){
        this.current = current;
        this.size = size;
    }

    public Page toMybatisPlusPage() {
        Page page = new Page<>(this.current, this.size);
        return page;
    }
}
