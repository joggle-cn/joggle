
package com.wuweibi.bullet.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * spring  工具类
 *
 * @author marker
 */
public class SpringUtil implements ApplicationContextAware {

    /** Spring上下文 */
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        SpringUtil.applicationContext = arg0;
    }

    /**
     * 根据类型获取bean
     *
     * @return
     * @throws
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据beanname获取bean
     *
     * @param name beanName
     * @return
     * @throws
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    /**
     * 根据beanname获取bean和类型获取
     *
     * @param name beanName
     * @return
     * @throws
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }


    /**
     * 是否为生产环境
     * @return
     */
    public static boolean isProduction(){
        Environment env = getBean(Environment.class);
        String active = env.getProperty("spring.profiles.active");
        if("prod".equals(active)){
            return true;
        }
        return false;
    }

    /**
     * 是否为生产环境
     * @return
     */
    public static String getProfilesActive(){
        Environment env = getBean(Environment.class);
        String active = env.getProperty("spring.profiles.active");
        return active;
    }

}
