package com.postsquad.scoup.web.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
class ObjectMapperUtilsTest {

    @Autowired
    ObjectMapperUtils objectMapperUtils;

    @ParameterizedTest
    @MethodSource("convertRequestParameterToObjectProvider")
    void convertRequestParameterToObject(
            Map<String, String[]> givenParameterMap,
            Class<?> givenTypeToConvert,
            Object expectedConvertedObject
    ) {
        // when
        Object actualConvertedObject = objectMapperUtils.convertRequestParameterToObject(givenParameterMap, givenTypeToConvert);

        // then
        then(actualConvertedObject).usingRecursiveComparison()
                                   .isEqualTo(expectedConvertedObject);
    }

    static Stream<Arguments> convertRequestParameterToObjectProvider() {
        return Stream.of(
                Arguments.arguments(
                        Map.of(
                                "start_date", new String[]{"2021-09-10"},
                                "end_date", new String[]{"2021-09-10"}
                        ),
                        RequestDtoForConvertRequestParameterToObjectTest.class,
                        new RequestDtoForConvertRequestParameterToObjectTest(
                                LocalDate.of(2021, 9, 10),
                                LocalDate.of(2021, 9, 10)
                        )
                )
        );
    }
}
