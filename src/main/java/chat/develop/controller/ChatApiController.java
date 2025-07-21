package chat.develop.controller;

import chat.develop.dto.ChatMessage;
import chat.develop.dto.ChatroomDto;
import chat.develop.entity.Chatroom;
import chat.develop.entity.Member;
import chat.develop.entity.Message;
import chat.develop.service.ChatService;
import chat.develop.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @PostMapping
    public ChatroomDto createChatroom(
        @AuthenticationPrincipal CustomOAuth2User user,
        @RequestParam String title
    ){
        Chatroom chatroom = chatService.createChatroom(user.getMember(), title);
        return ChatroomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(
        @AuthenticationPrincipal CustomOAuth2User user,
        @PathVariable("chatroomId") Long chatroomId,
        @RequestParam(required = false) Long currentChatroomId
    ){
        return chatService.joinChatroom(user.getMember(), chatroomId, currentChatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean deleteChatroom(
        @AuthenticationPrincipal CustomOAuth2User user,
        @PathVariable("chatroomId") Long chatroomId
    ) {
        return chatService.leaveChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<ChatroomDto> getChatroomList(
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        List<Chatroom> chatroomList = chatService.getChatroomList(user.getMember());

        return chatroomList.stream()
                .map(ChatroomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessageList(
        @PathVariable("chatroomId") Long chatroomId
    ) {
        List<Message> messageList = chatService.getMessageList(chatroomId);
        return messageList.stream()
                .map(message -> new ChatMessage(message.getMember().getNickName(), message.getText()))
                .toList();
    }
}
