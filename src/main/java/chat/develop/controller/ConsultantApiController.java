package chat.develop.controller;

import chat.develop.dto.ChatroomDto;
import chat.develop.dto.MemberDto;
import chat.develop.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consultants")
@RequiredArgsConstructor
@Slf4j
public class ConsultantApiController {

    private final ConsultantService consultantService;

    @PostMapping
    @ResponseBody
    public MemberDto saveMember(
        @RequestBody MemberDto memberDto
    ) {
        return consultantService.saveMember(memberDto);
    }

    @GetMapping
    public String index() {
        return "consultants/index.html";
    }

    @ResponseBody
    @GetMapping("/chats")
    public Page<ChatroomDto> getChatroomPage(Pageable pageable) {
        return consultantService.getChatroomPage(pageable);
    }
}
