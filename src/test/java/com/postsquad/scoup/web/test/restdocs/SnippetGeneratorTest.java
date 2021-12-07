package com.postsquad.scoup.web.test.restdocs;

import com.postsquad.scoup.web.common.FieldDescription;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Snippet;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

class SnippetGeneratorTest {

    static class TestSuperDto {

        @FieldDescription("description for id")
        private int id;
    }

    static class TestDto extends TestSuperDto {

        @FieldDescription(value = "description for name", optional = true)
        @NotEmpty
        @Size(min = 1, max = 5)
        private String name;
    }

    @Test
    void requestFields() {
        SnippetGenerator<TestDto> testDtoSnippetGenerator = new SnippetGenerator<>(TestDto.class);
        Snippet expectedRequestFieldsSnippet = PayloadDocumentation.requestFields(
                fieldWithPath("id")
                        .type("int")
                        .description("description for id")
                        .attributes(key("constraints").value(List.of())),
                fieldWithPath("name")
                        .type("String")
                        .description("description for name")
                        .attributes(key("constraints").value(List.of(
                                "Must not be empty",
                                "Size must be between 1 and 5 inclusive"
                        )))
                        .optional()
        );

        Snippet actualRequestFieldsSnippet = testDtoSnippetGenerator.requestFields();

        assertThat(actualRequestFieldsSnippet)
                .usingRecursiveComparison()
                .isEqualTo(expectedRequestFieldsSnippet);
    }

    @Test
    void responseFields() {
        SnippetGenerator<TestDto> testDtoSnippetGenerator = new SnippetGenerator<>(TestDto.class);
        Snippet expectedRequestFieldsSnippet = PayloadDocumentation.responseFields(
                fieldWithPath("id")
                        .type("int")
                        .description("description for id")
                        .attributes(key("constraints").value(List.of())),
                fieldWithPath("name")
                        .type("String")
                        .description("description for name")
                        .attributes(key("constraints").value(List.of(
                                "Must not be empty",
                                "Size must be between 1 and 5 inclusive"
                        )))
                        .optional()
        );

        Snippet actualRequestFieldsSnippet = testDtoSnippetGenerator.responseFields();

        assertThat(actualRequestFieldsSnippet)
                .usingRecursiveComparison()
                .isEqualTo(expectedRequestFieldsSnippet);
    }

    @Test
    void requestParameters() {
        SnippetGenerator<TestDto> testDtoSnippetGenerator = new SnippetGenerator<>(TestDto.class);
        Snippet expectedRequestParametersSnippet = RequestDocumentation.requestParameters(
                parameterWithName("id")
                        .description("description for id")
                        .attributes(key("constraints").value(List.of())),
                parameterWithName("name")
                        .description("description for name")
                        .attributes(key("constraints").value(List.of(
                                "Must not be empty",
                                "Size must be between 1 and 5 inclusive"
                        )))
                        .optional()
        );

        Snippet actualRequestParametersSnippet = testDtoSnippetGenerator.requestParameters();
        assertThat(actualRequestParametersSnippet)
                .usingRecursiveComparison()
                .isEqualTo(expectedRequestParametersSnippet);
    }
}
