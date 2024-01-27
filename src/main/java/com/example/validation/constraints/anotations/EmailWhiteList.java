package com.example.validation.constraints.anotations;


import com.example.validation.constraints.EmailWhiteListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target
// : 이 어노테이션(= 주석)이 어디에 붙일 수 있는지
@Target(ElementType.FIELD) // FIELD는 클래스의 속성에 붙인다는 의미다.
// @Retention
// : Retention(= 유지)은 언제까지 어노테이션이 남아있는지
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailWhiteListValidator.class)
public @interface EmailWhiteList {
  // Bean validation을 할 때, validation 라이브러리에서
  // constraint를 만들고 싶으면 이러한 조건들을 붙여줘야 한다.
  String message() default  "Email not in whiteList";
  Class<?>[] groups() default {};
  // 거의 안쓰기 때문에 필요하다라고만 알아두고 똑같이 넣어주면 된다.
  // 궁금하면 찾아보기 (그렇게 중요하지 않음)
  Class<? extends Payload>[] payload() default {};
}
