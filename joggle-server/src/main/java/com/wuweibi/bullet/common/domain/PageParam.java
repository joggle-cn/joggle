package com.wuweibi.bullet.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "分页参数")
public class PageParam<T> {


    @Schema(description = "页码 默认 1")
    private Integer current = 1;

    @Schema(description = "条数 默认10")
    private Integer size = 10;

    public PageParam(){}

    public PageParam(Integer current, Integer size){
        this.current = current;
        this.size = size;
    }

    public Page<T> toMybatisPlusPage() {
        return new Page<>(this.current, this.size);
    }
}
