package com.example.validation.constraints.anotations;

import com.example.validation.constraints.EmailBlackListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 속성에 붙여줄 것이다.
@Retention(RetentionPolicy.RUNTIME) // 실행 중에도 남아있어야 한다.
@Constraint(validatedBy = EmailBlackListValidator.class)
public @interface EmailBlackList {
  String message() default "Email in blackList";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String[] blackList() default {};
}
