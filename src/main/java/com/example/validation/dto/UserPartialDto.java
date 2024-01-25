package com.example.validation.dto;

import com.example.validation.groups.MandatoryStep;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

// UserDto와 구분짓기 위해 생성
// Validated의 2번째 기능을 알아보기 위한 Dto
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPartialDto {
  // 이 두가지(username, password)는 회원가입 단계에서 반드시 첨부해야 하는 데이터
  // 단, 회원 정보 업데이트 단계에서는 반드시는 아니다.
  @Size(min = 8, groups = MandatoryStep.class) // .class: 클래스 정의 자체를 타입으로 전달한다.
  @NotNull(groups = MandatoryStep.class)
  private String username;
  @Size(min = 10, groups = MandatoryStep.class)
  @NotNull(groups = MandatoryStep.class)
  private String password;
  // => groups 항목에다가 MandatoryStep.class를 하게되면
  // 하나의 그룹으로 묶이게 된다.

  // 안티 패턴 문제 발생
  // : Dto 자체는 데이터만 상관이 있어야 하는데 어떠한 과정에서도 검증이 되는지도 알고 있어야 하게 되었다.
  // 따라서 데이터를 담기 위한 용도, 어느 단계에서 사용될 것임을 알아야 하는 용도 등이 되어서
  // 관심사 분리가 완벽히 되지 못했다.

  // 이 두가지(email, phone)는 회원가입 완료 후 추가 정보 기입 단계에서 첨부하는 데이터
  // 단, 추가정보 기입시에는 반드시 포함해야 한다.
  @NotNull
  @Email
  private String email;
  @NotNull
  private String phone;
}
