package chat.develop.service;

import chat.develop.entity.Chatroom;
import chat.develop.entity.Member;
import chat.develop.entity.MemberChatroomMapping;
import chat.develop.entity.Message;
import chat.develop.repository.ChatroomRepository;
import chat.develop.repository.MemberChatroomMappingRepository;
import chat.develop.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomMappingRepository memberChatroomMappingRepository;
    private final MessageRepository messageRepository;

    // 채팅방 생성
    public Chatroom createChatroom(Member member, String title) {
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createdAt(LocalDateTime.now())
                .build();

        chatroom = chatroomRepository.save(chatroom);

        // 사용자는 자신이 만든 채팅방에 참여(Default)
        MemberChatroomMapping memberChatroomMapping = chatroom.addMember(member);

        memberChatroomMappingRepository.save(memberChatroomMapping);

        return chatroom;
    }

    // 다른사람이 만든 채팅방에 참여
    public Boolean joinChatroom(Member member, Long chatroomId) {
        if(memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)) {
            log.info("이미 참여한 채팅방 입니다");
            return false;
        }

        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroom not found with ID: " + chatroomId));

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberChatroomMappingRepository.save(memberChatroomMapping);

        return true;
    }

    // 이미 참여했던 방에서 나오는 로직
    @Transactional
    public Boolean leaveChatroom(Member member, Long chatroomId) {
        if(!memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)) {
            log.info("참여하지 않은 방입니다");
            return false;
        }

        memberChatroomMappingRepository.deleteByMemberIdAndChatroomId(member.getId(), chatroomId);

        return true;
    }

    // 사용자가 참여한 모든 채팅 목록
    public List<Chatroom> getChatroomList(Member member) {
        List<MemberChatroomMapping> memberChatroomMappingList = memberChatroomMappingRepository.findAllByMemberId(member.getId());

        return memberChatroomMappingList.stream()
                .map(MemberChatroomMapping::getChatroom)
                .toList();
    }

    // 메시지 저장
    public Message saveMessage(Member member, Long chatroomId, String text) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다"));

        Message message = Message.builder()
                .member(member)
                .chatroom(chatroom)
                .text(text)
                .build();

        return messageRepository.save(message);
    }

    // 특정 채팅방에서 작성된 모든 메시지 출력
    public List<Message> getMessageList(Long chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }
}
