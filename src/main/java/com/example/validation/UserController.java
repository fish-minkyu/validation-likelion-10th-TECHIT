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
    UserDto dto // Error 시, MethodArgumentNotValid 발생
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
    Integer age // Error 시, ConstraintViolationException 발생
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

  // @ExceptionHandler()는 어떤 에러가 발생했는지에 따라 실행될지 말지 결정된다.
  // 즉, 어떤 예외냐의 따라서 실행되는 @ExceptionHandler가 다르다는 이야기다.

  // 검증 실패시 응답하기 1번 - @Valid
  // MethodArgumentNotValidException는 400에러를 반환한다.
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleValidationException(
    final MethodArgumentNotValidException exception
  ) {
    // log.warn(), 예외를 조사하고 싶을 때 ExceptionHandler에서 log.warn() 찍어보기
    Map<String, Object> errors = new HashMap<>();
    // 예외가 가진 데이터를 불러오기
    // : 예외가 발생하면서 어떠한 데이터 인해 예외가 발생했는지 확인
    List<FieldError> fieldErrors
      = exception.getBindingResult().getFieldErrors();
    // getBindingResult()
    // : 데이터를 객체로 옮기는 과정에서 생겨난 결과를 모아놓은 곳

    // 각각의 에러에 대해서 순회하며
    for (FieldError error: fieldErrors) {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      // Map에 추가하기
      errors.put(fieldName, errorMessage);
    }

    // 여러개의 에러가 있다면 한번에 응답을 해주게 된다.
    return errors;
  }

  // 검증 실패시 응답하기 2번 - @Validated
  // ConstraintViolationException은 500 에러로 처리되므로 반드시 예외처리를 직접 해줘야 한다.
  // (클라이언트 잘못인데 서버 잘못으로 응답이 되므로 해줘야 한다.)
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleConstraintException(
    final ConstraintViolationException exception
  ) {
    Map<String, Object> errors = new HashMap<>();
    Set<ConstraintViolation<?>> violations // ?: 와일드 카드로서 어떤 타입이든 들어올 수 있다.
      = exception.getConstraintViolations();

    for (ConstraintViolation<?> violation: violations) {
      String property = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(property, message);
    }
    return errors;
  }
}

// 어노테이션이 너무 많다면 유효성 검사를 비즈니스 로직에 처리해도 된다.
// 유효성 검사의 목적은 사용자가 이상한 데이터를 넣었을 때 서비스를 망치게 하지 않는것이다.

// 1. Controller 단위에서 유효성 검사
// 2. Service 단위에서 유효성 검사
// 3. DB 제약사항을 통해 유효성 검사
// => 이렇게 3단계에 걸쳐 유효성 검사를 할 수 있다.
