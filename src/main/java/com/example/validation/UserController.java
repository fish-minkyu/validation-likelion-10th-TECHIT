package com.example.validation;

import com.example.validation.dto.UserDto;
import com.example.validation.dto.UserPartialDto;
import com.example.validation.groups.MandatoryStep;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
// @Validated
// : 클래스 단위에다가 @Validated를 붙이고
// 메서드의 매개변수에다가 내가 검증하고 싶은 기준을 넣어주면 된다.
// 즉, 메서드의 파라미터를 검증하고 싶다면 @Validated를 사용한다.
// 기능 A. 클래스에 붙이면 메서드 개별 파라미터의 검증을 진행할 수 있다.
// 기능 B. 필요한 부분만 따로 검사해서 실행
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
  private UserService service;

  // 클래스 단위에다가 @Validated를 붙이지 않았을 떼 메서드 Validation
  @PostMapping("/validate-dto")
  public String validateDto(
    // @Valid
    // : 요청 받을 때, 이 데이터는 입력을 검증해야 한다.(Dto는 입력받는 단계에서 검증하는 과정을 거친다.)
    // 검증하고 싶은 순간 @Valid 어노테이션이 있어야지만 validation을 할 수 있다.
    @Valid
    @RequestBody
    UserDto dto
  ) {
    // 개발자가 원하는대로 사용자가 입력값을 주지 않을 수 있다.
    log.info(dto.toString());
    return "done";
  }

  // 클래스 단위에다가 @Validated를 적용했을 때 메서드 Validation
  // @Validated가 붙은 클래스의 메서드 파라미터는 검증이 가능하다.
  // /validate-params?age=14
  @GetMapping("/validate-params")
  public String validateParams(
    // Validated + Param을 사용하는 상황에서 발생하는 예외는 조금 다른 예외가 발생한다.
    // 이 예외는 Spring Boot에서 자동으로 처리되는 예외가 아니다.
    // => 사용자가 잘못했지만 서버가 잘못했다는 500에러가 발생하므로 반드시 수동으로 에러 처리를 해줘야한다.
    @Min(14)
    @RequestParam("age")
    Integer age
  ) {
    log.info(age.toString());
    // @Validated가 어느 클래스든 사용이 가능하다고 테스트하기 위해 service의 메소드 호출
    service.printAge(age);
    return "done";
  }

  // /validate-man으로 요청할 때는 username과 password에 대한 검증만 진행하고 싶다.
  @PostMapping("/validate-man") // 1차적으로 반드시 필요한 정보를 기입하는 메소드
  public String validateMan(
    // 기능 B. 필요한 부분만 따로 검사해서 실행
    // : Dto의 일부분만 따로 검사를 하고 사용할 수 있다.
    @Validated(MandatoryStep.class)
    @RequestBody
    UserPartialDto dto
  ) {
    log.info(dto.toString());
    return "done";
  }
  
  // TODO /validate-add 해서 email과 phone이 필수적으로 들어가도록 해보기

  // 검증 실패시 응답하기 1.
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleValidationException(
    final MethodArgumentNotValidException exception
  ) {
    Map<String, Object> errors = new HashMap<>();
    // 예외가 가진 데이터를 불러오기
    List<FieldError> fieldErrors
      = exception.getBindingResult().getFieldErrors();

    // 각각의 에러에 대해서 순회하며
    for (FieldError error: fieldErrors) {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    }

    return errors;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleConstraintException(
    final ConstraintViolationException exception
  ) {
    Map<String, Object> errors = new HashMap<>();
    Set<ConstraintViolation<?>> violations // ?: <- 15:09 다시 보기
      = exception.getConstraintViolations();

    for (ConstraintViolation<?> violation: violations) {
      String property = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(property, message);
    }
    return errors;
  }
}
