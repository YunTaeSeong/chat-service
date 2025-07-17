package chat.develop.repository;

import chat.develop.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
