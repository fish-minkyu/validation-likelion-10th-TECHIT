package com.example.validation.constraints;

import com.example.validation.constraints.anotations.EmailBlackList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class EmailBlackListValidator
  implements ConstraintValidator<EmailBlackList, String> {

  private Set<String> blackList;

  @Override
  public void initialize(EmailBlackList anotation) {
    this.blackList = new HashSet<>();
    for (String blocked: anotation.blackList()) {
      this.blackList.add(blocked);
    }
  }

  // 10:23, implements하면 왜 빨간줄 뜨는지 이유 적어주기
  // alt + enter 누르면 자동으로 만들어짐
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // value가 null인지 체크하고, (null이면 false)
    if (value == null) return false;
    // value에 @가 포함되고 있는지 확인하고, (아니면 false)
    if (!value.contains("@")) return false;
    // value에 @을 기준으로 자른 뒤, 재일 뒤가 'this.blackList'에
    // 담긴 값이 아닌지 확인을 한다.
    String[] split = value.split("@");
    String domain = split[split.length -1];
    return !blackList.contains(domain);
  }
}
