package allclear.dto.responseDto.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Step8ResponseDto {
    private int currentPage; //현재 페이지
    private int pageSize; //페이지 사이즈
    private int totalPage; //전체 페이지
    private int totalElement; //전체 아이템 개수
    private List<TimetableResponseDto> timetableResponseDto;

    public Step8ResponseDto(Page<TimetableGeneratorTimetable> timetablePage) {
        this.timetableResponseDto = timetablePage
                .stream()
                .map(TimetableResponseDto::new)
                .collect(Collectors.toList());
        this.currentPage = timetablePage.getNumber();
        this.pageSize = timetablePage.getSize();
        this.totalPage = timetablePage.getTotalPages();
        this.totalElement = timetablePage.getNumberOfElements();
    }
}
