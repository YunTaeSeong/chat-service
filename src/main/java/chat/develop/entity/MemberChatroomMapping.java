package chat.develop.entity;

import jakarta.persistence.*;

// 어떠한 사용자가 어떤 채팅방에 참여 했는지?
@Entity
@Table(name = "member_chatroom_mapping")
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


}
