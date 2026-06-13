//package com.wuweibi.bullet.config.security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.StringUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class CustomTokenRequestConverter implements AuthenticationConverter {
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        // 支持将client参数放在header或body中
//        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//
//        if (StringUtils.hasText(grantType)) {
//            MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
//
//            // 处理客户端认证
//            String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
//            String clientSecret = parameters.getFirst(OAuth2ParameterNames.CLIENT_SECRET);
//
//            // 如果没有在参数中，尝试从header中获取
//            if (!StringUtils.hasText(clientId)) {
//                String authorization = request.getHeader("Authorization");
//                if (StringUtils.hasText(authorization) && authorization.startsWith("Basic ")) {
//                    // 解析Basic Auth
//                }
//            }
//
//            // 构建认证请求
//            Map<String, Object> additionalParameters = new HashMap<>();
//            parameters.forEach((key, value) -> {
//                if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
//                        !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
//                        !key.equals(OAuth2ParameterNames.CLIENT_SECRET)) {
//                    additionalParameters.put(key, value.get(0));
//                }
//            });
//
//            // 返回相应的认证token
//            // 实际实现需要根据 grantType 返回不同的 Authentication
//        }
//
//        return null;
//    }
//}
