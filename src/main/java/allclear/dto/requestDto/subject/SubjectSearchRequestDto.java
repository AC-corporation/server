package allclear.dto.requestDto.subject;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SubjectSearchRequestDto {

    @Nullable
    private String year; //수강 학년
    @Nullable
    private String searchString; //아무 검색 기능
    @Nullable
    private String courseClassification; // 과목 분류(ex : 전공 기초/필수, 교양선택, 교양필수, 채플 ...)

    // 전공 과목 해당 변수
    @Nullable
    private String major; // 학과(ex : 소프트웨어학부, 컴퓨터학부 ...)

    // 교양선택 해당 변수
    @Nullable
    private String optionalSubjectLiberalArtsClassificationYear; // 교양선택 이수 구분 해당년도 (ex : '15이전, '16-'18...)
    @Nullable
    private String optionalSubjectLiberalArtsClassification; // 교양선택 이수 구분  분류 (ex : 창의 융합)

    // 교양필수 해당 변수
    @Nullable
    private String requiredSubjectLiberalArtsClassification; // 교양필수 이수 구분 분류
    @Nullable
    private String requiredSubjectName; // 교양필수 해당 과목명
}
