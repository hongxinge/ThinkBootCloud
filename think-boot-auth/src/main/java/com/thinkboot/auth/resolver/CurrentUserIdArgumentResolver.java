package com.thinkboot.auth.resolver;

import com.thinkboot.auth.annotation.CurrentUserId;
import com.thinkboot.auth.context.UserContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * CurrentUserId 参数解析器
 * 
 * 自动将 @CurrentUserId 注解的方法参数注入为当前登录用户 ID。
 * 
 * 配置方式：
 * 在 WebMvcConfig 中注册此解析器：
 * registry.addArgumentResolver(new CurrentUserIdArgumentResolver());
 */
public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class) &&
               parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return UserContext.getCurrentUserId();
    }
}
