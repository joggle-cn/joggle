package com.wuweibi.bullet.oauth2.manager.impl;
/**
 * Created by marker on 2019/8/22.
 */

import com.wuweibi.bullet.oauth2.domain.Resource;
import com.wuweibi.bullet.oauth2.manager.NewMvcRequestMatcher;
import com.wuweibi.bullet.oauth2.manager.ResourceManager;
import com.wuweibi.bullet.oauth2.service.Oauth2ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * ResourceManager（资源管理器)
 *
 * @author marker
 * @create 2019-08-22 22:18
 **/
@Slf4j
@Component
public class ResourceManagerImpl implements ResourceManager {

    @Autowired
    private Oauth2ResourceService oauth2ResourceService;

    /**
     * 系统中所有权限集合
     */
    private Map<RequestMatcher, ConfigAttribute> resourceConfigAttributes;

    @Autowired
    private HandlerMappingIntrospector mvcHandlerMappingIntrospector;



    @Override
    public void saveResource(Resource resource) {
        resourceConfigAttributes.put(
                this.newMvcRequestMatcher(resource.getUrl(), resource.getMethod()),
                new SecurityConfig(resource.getCode())
        );
        log.info("resourceConfigAttributes size:{}", this.resourceConfigAttributes.size());
    }

    @Override
    public void removeResource(Resource resource) {
        resourceConfigAttributes.remove(this.newMvcRequestMatcher(resource.getUrl(), resource.getMethod()));
        log.info("resourceConfigAttributes size:{}", this.resourceConfigAttributes.size());
    }



    /**
     * 加载权限资源数据
     */
    public Map<RequestMatcher, ConfigAttribute> loadResource() {
        Set<Resource> resources = oauth2ResourceService.findAll();
        this.resourceConfigAttributes = resources.stream()
                .collect(Collectors.toMap(
                        resource -> this.newMvcRequestMatcher(resource.getUrl(), resource.getMethod()),
                        resource -> new SecurityConfig(resource.getCode())
                ));
        log.debug("resourceConfigAttributes:{}", this.resourceConfigAttributes);
        return this.resourceConfigAttributes;
    }

    @Override
    public ConfigAttribute findConfigAttributesByUrl(HttpServletRequest authRequest) {
        if(this.resourceConfigAttributes == null){
            this.loadResource();
        }
        return this.resourceConfigAttributes.keySet().stream()
                .filter(requestMatcher -> requestMatcher.matches(authRequest))
                .map(requestMatcher -> this.resourceConfigAttributes.get(requestMatcher))
                .peek(urlConfigAttribute -> log.debug("url在资源池中配置：{}", urlConfigAttribute.getAttribute()))
                .findFirst()
                .orElse(new SecurityConfig("NONEXISTENT_URL"));
    }



    /**
     * 创建RequestMatcher
     *
     * @param url url
     * @param method method
     * @return
     */
    private MvcRequestMatcher newMvcRequestMatcher(String url, String method) {
        return new NewMvcRequestMatcher(mvcHandlerMappingIntrospector, url, method);
    }





}
