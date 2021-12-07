package com.postsquad.scoup.web.test.restdocs;

import com.google.common.base.CaseFormat;
import com.postsquad.scoup.web.common.FieldDescription;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

public class SnippetGenerator<T> {

    private final Class<T> clazz;
    private final List<Field> fields;

    public SnippetGenerator(Class<T> clazz, List<Field> fields) {
        this.clazz = clazz;
        this.fields = fields;
    }

    public static <T> SnippetGenerator<T> from(Class<T> clazz) {
        return new SnippetGenerator<>(clazz, fieldsFrom(clazz));
    }

    private static List<Field> fieldsFrom(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> superClass = clazz.getSuperclass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            fields.addAll(List.of(superClass.getDeclaredFields()));
        }

        fields.addAll(List.of(clazz.getDeclaredFields()));

        return Collections.unmodifiableList(fields);
    }

    public Snippet requestFields() {
        return PayloadDocumentation.requestFields(fieldDescriptors());
    }

    public Snippet responseFields() {
        return PayloadDocumentation.responseFields(fieldDescriptors());
    }

    private List<FieldDescriptor> fieldDescriptors() {
        return fields.stream()
                     .map(this::fieldDescriptorFrom)
                     .collect(Collectors.toList());
    }

    private FieldDescriptor fieldDescriptorFrom(Field field) {
        String path = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        FieldDescriptor fieldWithPath = fieldWithPath(path);

        fieldWithPath.type(typeFrom(field));

        fieldWithPath.description(descriptionFrom(field, path));

        if (isOptional(field)) {
            fieldWithPath.optional();
        }

        fieldWithPath.attributes(constraintAttributeFrom(field));

        return fieldWithPath;
    }

    public Snippet requestParameters() {
        return RequestDocumentation.requestParameters(
                fields.stream()
                      .map(this::parameterDescriptorFrom)
                      .collect(Collectors.toList())
        );
    }

    private ParameterDescriptor parameterDescriptorFrom(Field field) {
        String path = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        ParameterDescriptor parameterWithName = parameterWithName(path);

        parameterWithName.description(descriptionFrom(field, path));

        if (isOptional(field)) {
            parameterWithName.optional();
        }

        parameterWithName.attributes(constraintAttributeFrom(field));

        return parameterWithName;
    }

    private String typeFrom(Field field) {
        return field.getType().getSimpleName();
    }

    private String descriptionFrom(Field field, String defaultDescription) {
        FieldDescription fieldDescription = field.getAnnotation(FieldDescription.class);

        if (Objects.nonNull(fieldDescription)) {
            return Objects.toString(fieldDescription.value(), defaultDescription);
        }

        return defaultDescription;
    }

    private boolean isOptional(Field field) {
        FieldDescription fieldDescription = field.getAnnotation(FieldDescription.class);
        return Objects.nonNull(fieldDescription) && fieldDescription.optional();
    }

    private Attributes.Attribute constraintAttributeFrom(Field field) {
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(clazz);

        return key("constraints").value(constraintDescriptions.descriptionsForProperty(field.getName()));
    }
}
