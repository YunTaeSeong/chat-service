package chat.develop.controller;

import chat.develop.dto.MemberDto;
import chat.develop.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultants")
@RequiredArgsConstructor
@Slf4j
public class ConsultantApiController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping
    public MemberDto saveMember(
        @RequestBody MemberDto memberDto
    ) {
        return customUserDetailsService.saveMember(memberDto);
    }

    @GetMapping
    public String index() {
        return "consultants/index.html";
    }
}
