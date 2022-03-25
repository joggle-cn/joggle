package com.wuweibi.bullet.service.impl;/**
 * Created by marker on 2018/6/20.
 */

import com.wuweibi.bullet.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

/**
 *
 * 邮件服务
 *
 * @author marker
 * @create 2018-06-20 13:14
 **/
@Slf4j
@Service
public class MailServiceImpl implements MailService {


    private static final String NORMAL_SUBJECT = "Bullet服务通知";


    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean


    private FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();



    @Autowired
    private TaskExecutor taskExecutor;

    public static final String encoding = "UTF-8";

    // 本地语言(默认汉语)
    public static final Locale locale = Locale.CHINA;


    @Value("${spring.mail.username}")
    private String serverEmail;


    public MailServiceImpl() {
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
        } catch (Exception e) {
            log.error("", e);
        }

    }




    /**
     * 构建邮件内容，发送邮件。
     *
     */
    public void send(String email, Map<String, Object> map, String tpl) {
        String to = email;
        String text = transformHtml(tpl, map);
        this.taskExecutor.execute(new SendMailThread(to, NORMAL_SUBJECT, text));
    }

    public void send(String email, String subject, Map<String, Object> map, String tpl) {
        String to = email;
        String text = transformHtml(tpl, map);
        this.taskExecutor.execute(new SendMailThread(to, subject, text));
    }


    /**
     * 转换Html
     * @param tpl 模板
     * @param map Map数据
     * @return
     */
    private String transformHtml(String tpl, Map<String, Object> map){
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tpl);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (IOException e) {
            log.error("", e);
        } catch (TemplateException e) {
            log.error("", e);
        }
        return "";
    }


    /**
     * 批量发送邮件 模板发送
     * @param emails ; 间隔
     * @param title
     * @param map
     * @param content
     * @return
     */
    public String sendTemplate(String emails, String title, Map<String, Object> map, String content) {
        String to = emails;
        String text = "";
        try {
            Configuration cfg = freeMarkerConfigurer.getConfiguration();

            Template template  = new Template(title, content, cfg);
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (IOException e) {
            log.error("", e);
        } catch (TemplateException e) {
            log.error("", e);
        }
        this.taskExecutor.execute(new SendMailThread(to, title, text));
        return text;
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
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");

            InternetAddress from = new InternetAddress();
            try {
                from.setPersonal("Bullet服务中心");
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
            }
            from.setAddress(serverEmail);
            messageHelper.setFrom(from);

            if (subject != null) {
                messageHelper.setSubject(subject);
            } else {
                messageHelper.setSubject("");
            }
            String[] tos  = to.split(";");
            messageHelper.setTo(tos);
            messageHelper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("", e);
        }
    }



    /**
     * 发送邮件
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param file 文件
     */
    public void sendMailAndFile(String to, String subject, String content, File file) {

        try {
//            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//            javaMailSender.setHost(configMail.getSmtpServer());
//            javaMailSender.setPort(configMail.getSmtpPort());
//            javaMailSender.setPassword(configMail.getPassword());
//            javaMailSender.setUsername(configMail.getUsername());
//            Properties properties = new Properties();
//            properties.setProperty("mail.smtp.auth", "true");
//            javaMailSender.setJavaMailProperties(properties);


            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(serverEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(simpleMailMessage.getFrom());
            if (subject != null) {
                messageHelper.setSubject(subject);
            } else {
                messageHelper.setSubject(simpleMailMessage.getSubject());
            }
            String[] tos  = to.split(";");
            messageHelper.setTo(tos);
            messageHelper.setText(content, true);
            messageHelper.addAttachment(file.getName(), file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("", e);
        }
    }

}
