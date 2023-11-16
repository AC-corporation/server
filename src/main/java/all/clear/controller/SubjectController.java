package all.clear.controller;

import all.clear.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
}
