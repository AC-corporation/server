package all.clear.controller;

import all.clear.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;



    //@Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logout")
    public void logout(){

    }
}
