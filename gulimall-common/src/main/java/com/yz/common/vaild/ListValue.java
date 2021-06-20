package com.yz.common.vaild;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//JSR303自定义效验
@Documented
@Constraint(
        validatedBy = {ListValueConstraintValidator.class }
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {

    String message() default "{com.yz.common.vaild.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}
