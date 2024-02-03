package allclear.controller;

import allclear.dto.requestDto.timetable.CreateTimetableRequestDto;
import allclear.dto.requestDto.timetable.UpdateTimetableRequestDto;
import allclear.global.response.ApiResponse;
import allclear.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        Long timetableId = timetableService.createTimetable(userId, requestDto);
        return ApiResponse.onSuccess("시간표 생성에 성공했습니다", timetableId);
    }

    @Operation(summary = "시간표 업데이트")
    @PutMapping("/{timetableId}")
    public ApiResponse updateTimetable(@PathVariable Long timetableId,
                                       @RequestBody UpdateTimetableRequestDto requestDto) {
        timetableService.updateTimetable(timetableId, requestDto);
        return ApiResponse.onSuccess("시간표 업데이트에 성공했습니다");
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
        return ApiResponse.onSuccess("시간표 삭제에 성공했습니다");
    }
}
