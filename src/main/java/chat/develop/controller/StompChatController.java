package chat.develop.controller;

import chat.develop.dto.ChatMessage;
import chat.develop.service.ChatService;
import chat.develop.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}") // send는 이제 회원 정보를 통해서 받아올 수 있음 -> CustomOAuth2UserService 구현
    public ChatMessage handleMessage(
        @AuthenticationPrincipal Principal principal,
        @DestinationVariable("chatroomId") Long chatroomId,
        @Payload Map<String, String> payload
    ) {
        log.info("{} send {} in {}", principal.getName(), payload, chatroomId);

        CustomOAuth2User user = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));
        return new ChatMessage(principal.getName(), payload.get("message"));
    }
}
