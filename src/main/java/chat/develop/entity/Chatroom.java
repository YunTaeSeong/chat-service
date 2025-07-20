package chat.develop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// 채팅방 개설, 참여, 탈퇴, 목록, 같은 채팅방에서만 메시지 확인 가능
@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chatroom")
    private Set<MemberChatroomMapping> memberChatroomMappingSet;

    private LocalDateTime createdAt;

    public MemberChatroomMapping addMember(Member member) {
        if(this.memberChatroomMappingSet == null) {
            this.memberChatroomMappingSet = new HashSet<>();
        }

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberChatroomMappingSet.add(memberChatroomMapping);

        return memberChatroomMapping;
    }
}
