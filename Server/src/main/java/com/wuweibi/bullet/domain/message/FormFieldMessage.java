package com.wuweibi.bullet.domain.message;

import cn.com.wuweiit.alias.State;
import cn.com.wuweiit.annotation.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * //description
 * 表单消息
 *
 * @author marker
 * @version 1.0
 * @date 2016/5/5 16:52
 */
public class FormFieldMessage {
    /** 日志记录器 */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /** 消息内容缓存区 */
    private static final Map<Integer, String> messages = new HashMap< >();



    /** 字段 */
    private String field;

    /** 状态码 */
    private Integer status;

    /** 消息内容 */
    private String msg;

    public FormFieldMessage() {
    }

    /**
     * 构造消息
     * @param field 字段
     * @param status 状态
     */
    public FormFieldMessage(String field, Integer status) {
        this.field = field;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


    /**
     * 获取消息内容
     * @return
     */
    public String getMsg() {
        if(null == msg ){
            try {
                if(messages.size() == 0){
                    getClass();
                    @SuppressWarnings("static-access")
                    Field[] fields =  State.class.getDeclaredFields();
                    for(Field field : fields){
                        Text t = field.getAnnotation(Text.class);
                        if(t != null){
                            messages.put((Integer)field.get(Integer.class), t.value());
                        }
                    }
                }
                return messages.get(this.status);
            } catch (Exception e) {
                logger.error("get MessageResult Info Error status="  +this.status , e);
            }
        }
        return msg;
    }
}
