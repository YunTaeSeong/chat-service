package chat.develop.controller;

import chat.develop.dto.ChatMessage;
import chat.develop.dto.ChatroomDto;
import chat.develop.service.ChatService;
import chat.develop.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;

    // 서버에서 Stomp 메시지를 발행
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}") // send는 이제 회원 정보를 통해서 받아올 수 있음 -> CustomOAuth2UserService 구현
    public ChatMessage handleMessage(
        @AuthenticationPrincipal Principal principal,
        @DestinationVariable("chatroomId") Long chatroomId,
        @Payload Map<String, String> payload
    ) {
        log.info("{} send {} in {}", principal.getName(), payload, chatroomId);

        // CustomOAuth2User user = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        // 상담사(유저) : UsernamePasswordAuthenticationToken || 회원(Oauth2) : OAuth2AuthenticationToken -> 다르기 때문에 상담사 send {message=님이 방에 들어왔습니다.} -> 에러
        // 해결방법 : 둘을 상속하는 AbstractAuthenticationToken 로 캐스팅하면 해결
        CustomOAuth2User user = (CustomOAuth2User) ((AbstractAuthenticationToken) principal).getPrincipal();
        chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));
        messagingTemplate.convertAndSend("/sub/chats/updates", chatService.getChatroom(chatroomId)); // 사용자가 접속 시 새로운 메시지가 발행되었다는 알림 받음
        return new ChatMessage(principal.getName(), payload.get("message"));
    }
}
