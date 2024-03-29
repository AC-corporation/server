package allclear.dto.responseDto.subject;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import allclear.domain.subject.Subject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubjectListResponseDto {
    private int currentPage; // 현재 페이지
    private int currentElement; // 현재 페이지 아이템 개수
    private int pageSize; // 페이지 사이즈
    private int totalPage; // 전체 페이지
    private long totalElement; // 전체 아이템 개수
    private List<SubjectResponseDto> subjectResponseDtoList;

    public SubjectListResponseDto(Page<Subject> subjectPage) {
        List<SubjectResponseDto> subjectResponseDtoList =
                subjectPage.getContent().stream()
                        .map(SubjectResponseDto::new)
                        .collect(Collectors.toList());
        this.subjectResponseDtoList = subjectResponseDtoList;
        this.currentPage = subjectPage.getNumber();
        this.pageSize = subjectPage.getSize();
        this.totalPage = subjectPage.getTotalPages();
        this.currentElement = subjectPage.getNumberOfElements();
        this.totalElement = subjectPage.getTotalElements();
    }
}
