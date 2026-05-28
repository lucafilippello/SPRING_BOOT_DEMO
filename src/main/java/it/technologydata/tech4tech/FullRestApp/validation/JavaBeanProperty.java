package it.technologydata.tech4tech.FullRestApp.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = JavaBeanPropertyValidator.class)
public @interface JavaBeanProperty {
    Class<?> beanClass();
    String availableValues();
    String message() default "must be any of {availableValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
