package com.postsquad.scoup.web.config;

import com.postsquad.scoup.web.common.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 리퀘스트 파라미터(@RequestParam으로 바인딩)에 사용되는 객체를 ServletRequestDataBinder 대신 처리한다.
 * primitive type 이 아닌 경우 ObjectMapper 를 이용하여 매핑하도록 설정.
 * 내부 정책과 다르게 매핑되는 쿼리 파라미터 매핑이 가능해짐.<br/>
 * - ServletRequestDataBinder 에서 처리할 경우 리플렉션으로 처리된다. 따라서 내부 정책이 snake case 인 경우 일반적인 방법으로 인식불가능하다.<br/>
 * - 따라서 원시값이 아닌경우 ObjectMapper를 이용하면 해당 Mapper에 설정된 대로 매핑가능.
 */
@RequiredArgsConstructor
@Component
public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapperUtils objectMapperUtils;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.getParameterType().isPrimitive();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return objectMapperUtils.convertRequestParameterToObject(webRequest.getParameterMap(), parameter.getParameterType());
    }
}
