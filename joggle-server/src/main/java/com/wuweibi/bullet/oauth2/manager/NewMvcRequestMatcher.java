package com.wuweibi.bullet.oauth2.manager;

import com.google.common.base.Objects;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


/**
 * NewMvcRequestMatcher
 * @author marker
 */
@Getter
public class NewMvcRequestMatcher extends MvcRequestMatcher {

    private String pattern;
    private String method;

    public NewMvcRequestMatcher(HandlerMappingIntrospector introspector, String pattern, String method) {
        super(introspector, pattern);
        this.setMethod(resolveHttpMethod(method));
        this.pattern = pattern;
        this.method = method;
    }

    private HttpMethod resolveHttpMethod(String method) {
        if (method == null) {
            return null;
        }
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewMvcRequestMatcher that = (NewMvcRequestMatcher) o;
        return Objects.equal(pattern, that.pattern)
                &&  Objects.equal(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pattern, method);
    }
}
