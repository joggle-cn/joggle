package com.wuweibi.bullet.service;
/**
 * Created by marker on 2018/6/20.
 */

import java.io.File;
import java.util.Map;

/**
 * @author marker
 * @create 2018-06-20 13:13
 **/
public interface MailService {


    /**
     * 发送邮件
     * @param emails ;间隔
     * @param title
     * @param map
     * @param content
     * @return
     */
    public String sendTemplate(String emails, String title, Map<String, Object> map, String content);


    /**
     * 发送邮件
     * @param email
     * @param map
     * @param tpl
     */
    public void send(String email, Map<String, Object> map, String tpl);

    /**
     *  发送邮件
     * @param email 邮箱
     * @param subject 主题
     * @param map 数据
     * @param tpl 模板
     */
    public void send(String email, String subject, Map<String, Object> map, String tpl);


    /**
     * 发送邮件
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param file 文件
     */
    public void sendMailAndFile(String to, String subject, String content, File file);

}
