package com.example.validation.dto;

import com.example.validation.groups.MandatoryStep;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

// UserDto와 구분짓기 위해 생성
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPartialDto {
  // 이 두가지는 회원가입 단계에서 반드시 첨부해야 하는 데이터
  // 단, 회원 정보 업데이트 단계에서는 반드시는 아니다.
  @Size(min = 8, groups = MandatoryStep.class) // .class: 클래스에 대한 정의를 전달해준다. <- 14:31 다시 들어보기
  @NotNull(groups = MandatoryStep.class)
  private String username;
  @Size(min = 10, groups = MandatoryStep.class)
  @NotNull(groups = MandatoryStep.class)
  private String password;
  // => groups = MandatoryStep.class <- 14:31다시 들어보기

  // 이 두가지는 회원가입 완료 후 추가 정보 기입 단계에서 첨부하는 데이터
  // 단, 추가정보 기입시에는 반드시 포함해야 한다.
  @NotNull
  @Email
  private String email;
  @NotNull
  private String phone;
}
