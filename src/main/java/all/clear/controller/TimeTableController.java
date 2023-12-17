package all.clear.controller;

import all.clear.service.MemberService;
import all.clear.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final MemberService memberService;
}
