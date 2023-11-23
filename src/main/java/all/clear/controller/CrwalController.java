package all.clear.controller;
import all.clear.crwal.CrwalUserInfo;
import all.clear.domain.User;
import all.clear.domain.requirement.Requirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/Crawl")
public class CrwalController {
    @GetMapping("/crwalData")
    public String crwalData(@Valid UserIdPwdForm form, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "/crwalData";
        // 폼의 내용을 바탕으로 크롤링 객체 생성 및 크롤링
        CrwalUserInfo crwalUserInfo = new CrwalUserInfo();
        String usaintId = form.getUsaintId().toString();
        crwalUserInfo.loginUsaint(usaintId, form.getUsaintPassword());
        // 폼의 내용을 바탕으로 Requirement 객체 생성
        Requirement requirement = new Requirement();
        // 폼의 내용을 바탕으로 User 객체 생성
        User user = new User();

        return "redirect:/";
    }
}
