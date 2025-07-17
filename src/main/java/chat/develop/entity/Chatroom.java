package chat.develop.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatroom")
    private Set<MemberChatroomMapping> memberSet;

    private LocalDateTime createdAt;
}
