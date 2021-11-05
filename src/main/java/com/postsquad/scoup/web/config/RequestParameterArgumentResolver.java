package com.postsquad.scoup.web.config;

import com.postsquad.scoup.web.common.ObjectMapperUtils;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * 리퀘스트 파라미터(@RequestParam으로 바인딩)에 사용되는 객체를 ServletRequestDataBinder 대신 처리한다.
 * primitive type 이 아닌 경우 ObjectMapper 를 이용하여 매핑하도록 설정.
 * 내부 정책과 다르게 매핑되는 쿼리 파라미터 매핑이 가능해짐.<br/>
 * - ServletRequestDataBinder 에서 처리할 경우 리플렉션으로 처리된다. 따라서 내부 정책이 snake case 인 경우 일반적인 방법으로 인식불가능하다.<br/>
 * - 따라서 원시값이 아닌경우 ObjectMapper를 이용하면 해당 Mapper에 설정된 대로 매핑가능.<br/>
 * <br/>
 * RequestBody, ResponseBody 어노테이션이 붙은 경우 RequestResponseBodyMethodProcessor에서 처리하게 된다. 이를 상속하여 내부 로직을 그대로 사용하도록 함.<br/>
 * - HTTP 메세지가 아닌 argument만 변환하고 있는데, 추후 헤더 설정 등에서 막힐경우 확인 필요.<br/>
 * - 생성자에 Advice를 넣어주지 않고 있는데 해당 부분에서 오류발생 시 수정 필요.<br/>
 */
public class RequestParameterArgumentResolver extends RequestResponseBodyMethodProcessor {

    private ObjectMapperUtils objectMapperUtils;

    public RequestParameterArgumentResolver(ObjectMapperUtils objectMapperUtils, List<HttpMessageConverter<?>> converters) {
        super(converters);
        this.objectMapperUtils = objectMapperUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.getParameterType().isPrimitive();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        parameter = parameter.nestedIfOptional();
        Object arg = objectMapperUtils.convertRequestParameterToObject(webRequest.getParameterMap(), parameter.getParameterType());
        String name = Conventions.getVariableNameForParameter(parameter);

        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if (arg != null) {
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            if (mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }

        return adaptArgumentIfNecessary(arg, parameter);
    }
}
