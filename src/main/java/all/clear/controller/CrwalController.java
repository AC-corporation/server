package all.clear.controller;
import all.clear.crwal.CrwalToEntity;
import all.clear.crwal.CrwalUserInfo;
import all.clear.service.RequirementComponentService;
import all.clear.service.RequirementService;
import all.clear.service.UserService;
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
    private final UserService userService;
    private final RequirementService requirementService;
    private final RequirementComponentService requirementComponentService;

    @GetMapping("/crwalData")
    public String crwalData(@Valid UserIdPwdForm form, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "/crwalData";
        // 폼의 내용을 바탕으로 크롤링 객체 생성 및 크롤링
        CrwalUserInfo crwalUserInfo = new CrwalUserInfo();
        String usaintId = form.getUsaintId().toString();
        crwalUserInfo.loginUsaint(usaintId, form.getUsaintPassword());
        // 크롤링한 데이터를 엔티티로 변환 위한 객체생성
        CrwalToEntity crwalToEntity = new CrwalToEntity(userService, requirementService,requirementComponentService);
        // 폼의 내용을 바탕으로 Requirement 객체 저장
        crwalToEntity.makeRequirementComponentEntity(crwalUserInfo);
        // 폼의 내용을 바탕으로 User 객체 저장

        return "redirect:/";
    }
}
