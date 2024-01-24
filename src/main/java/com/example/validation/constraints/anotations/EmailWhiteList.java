package com.example.validation.constraints.anotations;


import com.example.validation.constraints.EmailWhiteListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 이 어노테이션이 어디에 붙일 수 있는지
@Target(ElementType.FIELD) // FIELD는 클래스의 속성에 붙인다는 의미다.
// Retention은 언제까지 어노테이션이 남아있는지
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailWhiteListValidator.class)
public @interface EmailWhiteList {
  // 9:36 다시 듣기 - 설명은 안한다고 했었음
  String message() default  "Email not in whitelist";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
