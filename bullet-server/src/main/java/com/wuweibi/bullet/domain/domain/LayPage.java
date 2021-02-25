package com.wuweibi.bullet.domain.domain;
/**
 * Created by marker on 2018/3/16.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 针对Layui的page对象
 *
 * @author marker
 * @create 2018-03-16 上午10:11
 **/
@Data
public class LayPage<T> implements Serializable {


    private int code;
    private String msg;
    private long count;
    private List<T> data;


    /**
     * 自定义构造
     * （转换数据结构）
     * @param page page对象
     */
    public LayPage(Page<T> page){
        if(page == null) return;
        this.code = 0;
        this.msg = "success";
        this.count = page.getTotal();
        this.data = page.getRecords();
    }



}
