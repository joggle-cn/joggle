package com.wuweibi.bullet.oauth2.manager;
/**
 * Created by marker on 2019/8/22.
 */
import com.wuweibi.bullet.oauth2.domain.Resource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * 资源管理器
 * @author marker
 * @create 2019-08-22 22:29
 **/
public interface ResourceManager {


    /**
     * 动态新增更新权限
     *
     * @param resource resource
     */
    void saveResource(Resource resource);

    /**
     * 动态删除权限
     *
     * @param resource resource
     */
    void removeResource(Resource resource);

    /**
     * 加载权限资源数据
     */
    Map<RequestMatcher, ConfigAttribute> loadResource();

    /**
     * 根据url和method查询到对应的权限信息
     *
     * @param authRequest authRequest
     * @return
     */
    ConfigAttribute findConfigAttributesByUrl(HttpServletRequest authRequest);



}
