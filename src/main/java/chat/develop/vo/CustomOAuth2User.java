package chat.develop.vo;

import chat.develop.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    @Getter
    private Member member;
    private Map<String, Object> map;

    @Override
    public Map<String, Object> getAttributes() {
        return this.map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> this.member.getRole());
    }

    @Override
    public String getName() {
        return member.getNickName();
    }
}
