package all.clear.controller;

import all.clear.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
}
