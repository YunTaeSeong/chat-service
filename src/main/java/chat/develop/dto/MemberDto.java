package chat.develop.dto;

import chat.develop.entity.Member;
import chat.develop.enums.GenderStatus;

import java.time.LocalDate;

public record MemberDto(
        Long id,
        String email,
        String nickName,
        String name,
        String password,
        String confirmedPassword, // 두번째 패스워드 입력
        GenderStatus gender,
        String phoneNumber,
        LocalDate birthDay,
        String role
) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getName(),
                null,
                null,
                member.getGender(),
                member.getPhoneNumber(),
                member.getBirthDay(),
                member.getRole()
        );
    }

    public static Member to(MemberDto memberDto) {
        return Member.builder()
                .id(memberDto.id())
                .email(memberDto.email())
                .nickName(memberDto.nickName())
                .name(memberDto.name())
                .gender(memberDto.gender())
                .phoneNumber(memberDto.phoneNumber())
                .birthDay(memberDto.birthDay())
                .role(memberDto.role())
                .build();
    }
}
