package chat.develop.service;

import chat.develop.dto.ChatroomDto;
import chat.develop.dto.MemberDto;
import chat.develop.entity.Chatroom;
import chat.develop.entity.Member;
import chat.develop.enums.Role;
import chat.develop.repository.ChatroomRepository;
import chat.develop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultantService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다"));

        if(Role.fromCode(member.getRole()) != Role.CONSULTANT) {
            throw new AccessDeniedException("상담사가 아닙니다");
        }

        return new CustomUserDetails(member, null);
    }

    // 유저를 Member 등록
    public MemberDto saveMember(MemberDto memberDto) {
        Member member = MemberDto.to(memberDto);
        member.updatePassword(memberDto.password(), memberDto.confirmedPassword(), passwordEncoder);
        memberRepository.save(member);

        return MemberDto.from(member);
    }

    public Page<ChatroomDto> getChatroomPage(Pageable pageable) {
        Page<Chatroom> chatroomPage = chatroomRepository.findAll(pageable);

        return chatroomPage.map(ChatroomDto::from);
    }
}
