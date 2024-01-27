package allclear.controller;

import allclear.dto.requestDto.timetableGenerator.*;
import allclear.global.response.ApiResponse;
import allclear.service.timetableGenerator.TimetableGeneratorManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "api 정보 제공하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/timetableGenerator")
public class TimetableGeneratorController {
    private final TimetableGeneratorManager timetableGeneratorManager;

    //==step1==//
    @Operation(summary = "학년도 선택")
    @PostMapping("/step1/{userId}")
    public ApiResponse step1(@PathVariable Long userId, @RequestBody Step1RequestDto requestDto) {
        timetableGeneratorManager.createTimetableGenerator(userId, requestDto);
        return ApiResponse.onSuccess("step1 성공했습니다", "");
    }

    //==step2==//
    @Operation(summary = "직접 추가")
    @PostMapping("/step2/{userId}")
    public ApiResponse step2(@PathVariable Long userId, @RequestBody Step2RequestDto requestDto) {
        timetableGeneratorManager.addCustomTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step2 성공했습니다", "");
    }

    //==step3==//
    @Operation(summary = "전공 기초/필수 과목 추천")
    @GetMapping("/step3/{userId}")
    public ApiResponse step3(@PathVariable Long userId) {
        return ApiResponse.onSuccess("step3 성공했습니다", "");
    }

    @Operation(summary = "전공 기초/필수")
    @PostMapping("/step3/{userId}")
    public ApiResponse step3(@PathVariable Long userId, @RequestBody Step3RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step3 성공했습니다", "");
    }

    //==step4==//
    @Operation(summary = "전공 기초/필수 과목 추천")
    @GetMapping("/step4/{userId}")
    public ApiResponse step4(@PathVariable Long userId) {
//        return ApiResponse.onSuccess("step4 성공했습니다", timetableGeneratorManager.suggestMajorSubject(userId));
        return null;
    }

    @Operation(summary = "교양 필수")
    @PostMapping("/step4/{userId}")
    public ApiResponse step4(@PathVariable Long userId, @RequestBody Step4RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step4 성공했습니다", "");
    }

    //==step5==//
    @Operation(summary = "전공 기초/필수 과목 추천")
    @GetMapping("/step5/{userId}")
    public ApiResponse step5(@PathVariable Long userId) {
        return ApiResponse.onSuccess("step5 성공했습니다", "");
    }

    @Operation(summary = "전공 선택")
    @PostMapping("/step5/{userId}")
    public ApiResponse step6(@PathVariable Long userId, @RequestBody Step5RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step5 성공했습니다", "");
    }

    //==step6==//
    @Operation(summary = "전공 기초/필수 과목 추천")
    @GetMapping("/step6/{userId}")
    public ApiResponse step6(@PathVariable Long userId) {
        return ApiResponse.onSuccess("step6 과목 추천 성공했습니다", "");
    }

    @Operation(summary = "교양 선택")
    @PostMapping("/step6/{userId}")
    public ApiResponse step6(@PathVariable Long userId, @RequestBody Step6RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step6 성공했습니다", "");
    }

    //==step7==//
    @Operation(summary = "지금까지 선택한 과목 불러오기")
    @PostMapping("/step7/{userId}")
    public ApiResponse step7(@PathVariable Long userId) {
        return ApiResponse.onSuccess("step7 성공했습니다", timetableGeneratorManager.getTimetableGeneratorSubjectList(userId));
    }

    @Operation(summary = "필수 수강 과목 선택/시간표 생성")
    @PostMapping("/step7/{userId}")
    public ApiResponse step7(@PathVariable Long userId, @RequestBody Step7RequestDto requestDto) {
        timetableGeneratorManager.checkSelectedTimetableSubject(requestDto);
        timetableGeneratorManager.generateTimetable(userId);
        return ApiResponse.onSuccess("step7 성공했습니다", "");
    }

    //==step8==//
    @Operation(summary = "생성된 시간표 불러오기")
    @GetMapping("/step8/{userId}")
    public ApiResponse step8(@PathVariable Long userId) {
        return ApiResponse.onSuccess("step8 성공했습니다", timetableGeneratorManager.getTimetableGeneratorTimetableList(userId));
    }

    @Operation(summary = "생성된 시간표 저장")
    @PostMapping("/step8/{userId}")
    public ApiResponse step8(@PathVariable Long userId, @RequestBody Step8RequestDto requestDto) {
        timetableGeneratorManager.saveTimetable(userId, requestDto);
        return ApiResponse.onSuccess("step8 성공했습니다", "");
    }

}
