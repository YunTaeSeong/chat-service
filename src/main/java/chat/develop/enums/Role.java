package chat.develop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),

    CONSULTANT("ROLE_CONSULTANT")
    ;

    private String code;

    // code로 부터 enum 반환
    public static Role fromCode(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst()
                .orElseThrow();
    }

}
