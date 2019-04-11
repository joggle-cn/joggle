package com.wuweibi.bullet.service.impl;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;


/**
 * 邮件发送工具
 * （包装了Freemarker模板引擎，可以快速设计邮件界面
 *    还有一个调度线程池
 *   ）
 * 
 * @author marker
 * @version 1.0
 */ 
@Service
public class MailSender {
	
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private SimpleMailMessage simpleMailMessage;
     
    private FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    
    @Autowired
    private TaskExecutor taskExecutor;
    
    public static final String encoding = "UTF-8";
	
    // 本地语言(默认汉语)
	public static final Locale locale = Locale.CHINA;
	
	
	
	
    public MailSender() { 
    	freeMarkerConfigurer.setTemplateLoaderPath("classpath:/mail/template");
    	 
    	try {
			freeMarkerConfigurer.afterPropertiesSet();
			Configuration config = freeMarkerConfigurer.getConfiguration();
			 
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			config.setDefaultEncoding(encoding);
		    config.setOutputEncoding(encoding);
			config.setEncoding(locale, encoding);// 
	        config.setLocale(locale);
	        config.setLocalizedLookup(false); 
			
			
			
		} catch ( Exception e) { 
			e.printStackTrace();
		}
    	
    	 
    	
    	
    	
	}
    
    /**
     * 构建邮件内容，发送邮件。
     * @param map  用户
     * @param tpl   链接
     */
    public void send(String email, Map<String,Object> map, String tpl) {
        String to = email;
        String text = "";
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tpl);
//            模板中用${XXX}站位，map中key为XXX的value会替换占位符内容。
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        this.taskExecutor.execute(new SendMailThread(to,null,text));
    }
    
    
    
    
    
    //    内部线程类，利用线程池异步发邮件。
    private class SendMailThread implements Runnable {
        private String to;
        private String subject;
        private String content;
        private SendMailThread(String to, String subject, String content) {
            super();
            this.to = to;
            this.subject = subject;
            this.content = content;
        } 
        public void run() {
            sendMail(to, subject, content);
        }
    }
    
    
    
    
    
    /**
     * 发送邮件
     * @param to        收件人邮箱
     * @param subject   邮件主题
     * @param content   邮件内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(simpleMailMessage.getFrom());
            if (subject != null) {
                messageHelper.setSubject(subject);
            } else {
                messageHelper.setSubject(simpleMailMessage.getSubject());
            }
            messageHelper.setTo(to);
            messageHelper.setText(content, true);
           javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
