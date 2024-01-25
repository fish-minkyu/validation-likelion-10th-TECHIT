package com.example.validation;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
// @Validated는 Controller에서만 사용할 수 있는 것이 아니다.느
// 어떤 클래스에서도 사용이 가능하다.
@Validated
public class UserService {
  public void printAge(
    @Min(19)
    Integer age
  ) {
    log.info(age.toString());
  }
}
