package chat.develop.controller;

import chat.develop.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
public class StompChatController {

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}") // send는 이제 회원 정보를 통해서 받아올 수 있음 -> CustomOAuth2UserService 구현
    public ChatMessage handleMessage(
        @AuthenticationPrincipal Principal principal,
        @DestinationVariable("chatroomId") Long chatroomId,
        @Payload Map<String, String> payload
    ) {
        log.info("{} send {} in {}", principal.getName(), payload, chatroomId);

        return new ChatMessage(principal.getName(), payload.get("message"));
    }
}
