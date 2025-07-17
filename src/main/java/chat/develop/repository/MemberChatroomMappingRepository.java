package chat.develop.repository;

import chat.develop.entity.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChatroomMappingRepository extends JpaRepository<MemberChatroomMapping, Long> {
}
