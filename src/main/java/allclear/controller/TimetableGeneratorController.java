package allclear.controller;

import allclear.dto.requestDto.timetableGenerator.Step1RequestDto;
import allclear.dto.requestDto.timetableGenerator.Step2RequestDto;
import allclear.dto.requestDto.timetableGenerator.Step3RequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.timetableGenerator.TimetableGeneratorManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags="api 정보 제공하는 컨트롤러")
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
    @Operation(summary = "전공 기초/필수")
    @PostMapping("/step3/{userId}")
    public ApiResponse step3(@PathVariable Long userId, @RequestBody Step3RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step3 성공했습니다", "");
    }

    //==step4==//
    @Operation(summary = "교양 필수")
    @PostMapping("/step4/{userId}")
    public ApiResponse step4(@PathVariable Long userId, @RequestBody Step3RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step4 성공했습니다", "");
    }

    //==step5==//
    @Operation(summary = "전공 선택")
    @PostMapping("/step5/{userId}")
    public ApiResponse step5(@PathVariable Long userId, @RequestBody Step3RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step5 성공했습니다", "");
    }

    //==step6==//
    @Operation(summary = "교양 선택")
    @PostMapping("/step6/{userId}")
    public ApiResponse step6(@PathVariable Long userId, @RequestBody Step3RequestDto requestDto) {
        timetableGeneratorManager.addActualTimetableGeneratorSubjects(userId, requestDto);
        return ApiResponse.onSuccess("step6 성공했습니다", "");
    }

//    @Operation(summary = "필수 수강 과목 선택")
//    @PostMapping("/step7/{userId}")
//    public ApiResponse step7() {
//        return ApiResponse.onSuccess("step7 성공했습니다", "");
//    }

}
