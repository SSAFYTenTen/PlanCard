package com.ssafy.backend.domain.member.dto;


import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberSignUpRequestDto {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    private String name;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String nickname;

    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address;

    // DTO -> Entity
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .address(address)
                .role(MemberRole.USER)
                .build();
    }


}
