package all.clear.controller;
import all.clear.crwal.CrawlToEntity;
import all.clear.crwal.CrawlMemberInfo;
import all.clear.service.RequirementService;
import all.clear.service.MemberService;
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
    private final MemberService memberService;
    private final RequirementService requirementService;

    @GetMapping("/crwalData")
    public String crwalData(@Valid MemberIdPwdForm form, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "/crwalData";
        // 폼의 내용을 바탕으로 크롤링 객체 생성 및 크롤링
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo();
        String usaintId = form.getUsaintId().toString();
        crawlMemberInfo.loginUsaint(usaintId, form.getUsaintPassword());
        // 크롤링한 데이터를 엔티티로 변환 위한 객체생성
        CrawlToEntity crawlToEntity = new CrawlToEntity(memberService, requirementService);
        // 폼의 내용을 바탕으로 Requirement 객체 저장
        crawlToEntity.makeRequirementComponentEntity(crawlMemberInfo);
        // 폼의 내용을 바탕으로 Member 객체 저장

        return "redirect:/";
    }
}
