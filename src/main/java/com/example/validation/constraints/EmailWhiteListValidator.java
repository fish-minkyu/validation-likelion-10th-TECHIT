package com.example.validation.constraints;

import com.example.validation.constraints.anotations.EmailWhiteList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

// 어노테이션이 붙은 타겟이 어떤 조건을 만족할지 validation
public class EmailWhiteListValidator implements ConstraintValidator<EmailWhiteList, String> {

  private final Set<String> whiteList;

  public EmailWhiteListValidator() {
    this.whiteList = new HashSet<>(); // Set을 쓰는 것이 성능적으로 좋다.
    this.whiteList.add("gmail.com");
    this.whiteList.add("naver.com");
    this.whiteList.add("kakao.com");
  }


  // EmailWhiteList 어노테이션이 붙은 대상의 데이터가
  // 검사를 통과하면 true를
  // 검사에 실패하면 false를
  // 반환하도록 만들면 된다.
  @Override
  public boolean isValid(
    // value: 실제로 사용자가 입력한 내용이 여기 들어간다.
    String value,
    ConstraintValidatorContext context
  ) {
    // value가 null인지 체크하고, (null이면 false)
    if (value == null) return false;
    // value에 @가 포함되고 있는지 확인하고, (아니면 false)
    if (!value.contains("@")) return false;
    // value에 @을 기준으로 자른 뒤, 재일 뒤가 'this.whiteList'에
    // 담긴 값 중 하나인지를 확인한다.
    String[] split = value.split("@");
    String domain = split[split.length -1];
    return whiteList.contains(domain);
  }
}
