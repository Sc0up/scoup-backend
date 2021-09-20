package com.postsquad.scoup.web.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ObjectMapperUtils {

    private final ObjectMapper objectMapper;

    public <T> T convertRequestParameterToObject(Map<String, String[]> parameterMap, Class<T> typeToConvert) {
        Map<String, Object> methodParameterForConvert = parameterMap.entrySet().parallelStream()
                                                                    .collect(Collectors.toMap(
                                                                            Map.Entry::getKey,
                                                                            mapSingleArrayValueToString()
                                                                    ));

        return objectMapper.convertValue(methodParameterForConvert, typeToConvert);
    }

    private Function<Map.Entry<String, String[]>, Object> mapSingleArrayValueToString() {
        return entry -> {
            if (entry.getValue().length == 1) {
                return entry.getValue()[0];
            }
            // 길이가 1이 아닌 경우는 배열로 반환하여 리스트로 매핑되도록 함.
            return entry.getValue();
        };
    }
}
