package all.clear.controller;

import all.clear.service.TimeTableService;
import all.clear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final UserService userService;
}
