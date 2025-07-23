package chat.develop.service;

import chat.develop.entity.Member;
import chat.develop.enums.GenderStatus;
import chat.develop.repository.MemberRepository;
import chat.develop.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> map = oAuth2User.getAttribute("kakao_account");

        assert map != null;
        String email = (String) map.get("email");

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> registerMember(map)); // Member 없을 경우 카카오 에서 받은 회원정보로 가입

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }


    private Member registerMember(Map<String, Object> map) {
        Member member = Member.builder()
                .email((String) map.get("email"))
                .nickName((String) ((Map<?, ?>)map.get("profile")).get("nickname"))
                .name((String) map.get("name"))
                .phoneNumber((String) map.get("phone_number"))
                .gender(GenderStatus.valueOf(((String) map.get("gender")).toUpperCase()))
                .birthDay(getBirthDay(map))
                .role("ROLE_USER")
                .build();

        return memberRepository.save(member);
    }

    private LocalDate getBirthDay(Map<String, Object> map) {
        String birthYear = (String) map.get("birthyear");
        String birthDay = (String) map.get("birthday");

        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }

}
