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
// 1. 클래스에 붙이면 메서드 개별 파라미터의 검증을 진행할 수 있다.
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
  private UserService service;

  @PostMapping("/validate-dto")
  public String validateDto(
    // @Valid
    // : 이 데이터는 입력을 검증해야 한다.(입력받는 단계에서 검증하는 과정을 거친다.)
    // 검증하고 싶은 순간 @Valid 어노테이션이 있어야지만 validation을 할 수 있다.
    @Valid // 13:31 다시 들어보기
    @RequestBody
    UserDto dto
  ) {
    // 개발자가 원하는대로 사용자가 입력값을 주지 않을 수 있다.
    log.info(dto.toString());
    return "done";
  }

  // /validate-params?age=14
  @GetMapping("/validate-params")
  public String validateParams(
    // @Validated가 붙은 클래스의 메서드 파라미터는 검증이 가능하다.
    // 14:12분 주의할 점 다시 적어두기
    // 주의할 점. 수동으로 에러 처리를 해줘야 한다.
    @Min(14)
    @RequestParam("age")
    Integer age
  ) {
    log.info(age.toString());
    service.printAge(age);
    return "done";
  }

  // /validate-man으로 요청할 때는 username과 password에 대한 검증한 진행하고 싶다.
  @PostMapping("/validate-man") // 1차적으로 반드시 필요한 정보를 기입
  public String validateMan(
    // 2. 필요한 부분만 따로 검사해서 실행
    // : Dto의 일부분만 따로 검사를 하고 사용할 수 있다.
    @Validated(MandatoryStep.class)
    @RequestBody
    UserPartialDto dto
  ) {
    log.info(dto.toString());
    return "done";
  }

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
