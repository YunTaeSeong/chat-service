package chat.develop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 어떠한 사용자가 어떤 채팅방에 참여 했는지?
@Entity
@Table(name = "member_chatroom_mapping")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberChatroomMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_chatroom_mapping_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    // lastCheckedAt 보다 createdAt이 더 늦은 메시지 -> 신규 메시지
    private LocalDateTime lastCheckedAt;

    public void updateLastCheckAt() {
        this.lastCheckedAt = LocalDateTime.now();
    }
}
