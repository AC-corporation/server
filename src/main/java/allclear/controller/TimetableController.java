package allclear.controller;

import allclear.dto.requestDto.timetable.*;
import allclear.global.response.ApiResponse;
import allclear.service.TimetableService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags="api 정보 제공하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/timetable")
public class TimetableController {
    private final TimetableService timetableService;


    //==시간표==//
    @Operation(summary = "시간표 생성")
    @PostMapping("/{userId}")
    public ApiResponse createTimetable(@PathVariable Long userId,
                                       @RequestBody CreateTimetableRequestDto requestDto) {
        Long memberId = timetableService.createTimetable(userId, requestDto);
        return ApiResponse.onSuccess("시간표 생성에 성공했습니다", memberId);
    }

    @Operation(summary = "시간표 업데이트")
    @PutMapping("/{timetableId}")
    public ApiResponse updateTimetable(@PathVariable Long timetableId,
                                       @RequestBody UpdateTimetableRequestDto requestDto) {
        timetableService.updateTimetable(timetableId, requestDto);
        return ApiResponse.onSuccess("시간표 업데이트에 성공했습니다", "");
    }

    @Operation(summary = "시간표 조회")
    @GetMapping("/{timetableId}")
    public ApiResponse getTimetable(@PathVariable Long timetableId) {
        return ApiResponse.onSuccess("시간표 조회에 성공했습니다", timetableService.getTimetable(timetableId));
    }

    @Operation(summary = "시간표 삭제")
    @DeleteMapping("/{timetableId}")
    public ApiResponse deleteTimetable(@PathVariable Long timetableId) {
        timetableService.deleteTimetable(timetableId);
        return ApiResponse.onSuccess("시간표 삭제에 성공했습니다", "");
    }


    //==시간표 과목==//
    @Operation(summary = "시간표 과목 추가")
    @PostMapping("/subject/actual/{timetableId}")
    public ApiResponse createTimetableSubject(@PathVariable Long timetableId,
                                              @RequestBody AddTimetableSubjectRequestDto requestDto) {
        Long timetableSubjectId = timetableService.addTimetableSubject(timetableId, requestDto);
        return ApiResponse.onSuccess("시간표 과목 생성에 성공했습니다", timetableSubjectId);
    }

    @Operation(summary = "시간표 과목 추가")
    @PostMapping("/subject/custom/{timetableId}")
    public ApiResponse createTimetableSubject(@PathVariable Long timetableId,
                                              @RequestBody AddCustomTimetableSubjectRequestDto requestDto) {
        Long timetableSubjectId = timetableService.addTimetableSubject(timetableId, requestDto);
        return ApiResponse.onSuccess("시간표 과목 생성에 성공했습니다", timetableSubjectId);
    }

    @Operation(summary = "시간표 과목 업데이트")
    @PutMapping("/subject/{timetableSubjectId}")
    public ApiResponse updateTimetableSubject(@PathVariable Long timetableSubjectId,
                                              @RequestBody UpdateTimetableSubjectRequestDto requestDto) {
        timetableService.updateTimetableSubject(timetableSubjectId, requestDto);
        return ApiResponse.onSuccess("시간표 과목 업데이트에 성공했습니다", "");
    }

    @Operation(summary = "시간표 과목 조회")
    @GetMapping("/subject/{timetableSubjectId}")
    public ApiResponse getTimetableSubject(@PathVariable Long timetableSubjectId) {
        return ApiResponse.onSuccess("시간표 과목 조회에 성공했습니다", timetableService.getTimetableSubject(timetableSubjectId));
    }

    @Operation(summary = "시간표 과목 삭제")
    @DeleteMapping("/subject/{timetableSubjectId}")
    public ApiResponse deleteTimetableSubject(@PathVariable Long timetableSubjectId) {
        timetableService.deleteTimetableSubject(timetableSubjectId);
        return ApiResponse.onSuccess("시간표 과목 삭제에 성공했습니다", "");
    }
}
