package com.example.validation.dto;

import com.example.validation.constraints.anotations.EmailBlackList;
import com.example.validation.constraints.anotations.EmailWhiteList;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// Dto는 사용자가 보내주는 Json데이터를 자바로 표현한 것이다.
public class UserDto {
  private Long id;
  // 사용자가 입력하는 데이터 중 검증하고 싶은 데이터

  // 필수값
  @NotNull
  // 사용자 계정은 8글자 이상
  // message를 기록하면 예외에 기록되는 문구를 바꿀 수 있다.
  @Size(min = 8, message = "8자는 넣어주세요.")
  private String username;
  @Email(message = "Email을 넣어주세요.")
//  @EmailWhiteList(message = "혀용된 도메인이 아닙니다.")
  @EmailBlackList(blackList = {"malware.good", "yahoo.com"}) // 복수개를 넣으려면 중괄호({})를 사용한다.
  private String email;
  // 14세 이상만 받아준다.
  @Min(value = 14 , message = "만 14세 이하는 부모님 동의가 필요합니다.")
  private Integer age;
  // LocalDate: 날짜를 나타내는 Java 클래스 ('YYYY-MM-DD')
  @Future // 미래 시간이어야 한다.
  private LocalDate validUntil;

  // NotNull vs NotEmpty vs NotBlank
  @NotNull // null만 아니면 된다. 즉 어떤 데이터를 넣어도 상관없다. Ex. ""
  private String notNullString;
  @NotEmpty // 비어있지만 않으면 된다. 즉 길이가 0만 아니면 된다. Ex. " "
  private String notEmptyString;
  @NotBlank // 내용물이 존재해야 동작한다. Ex. "a"
  private String notBlankString;
}
