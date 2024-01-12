package allclear.dto.requestDto.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GetSubjectListRequestDto {
    private Long subjectId; //과목 번호
    private String subjectName; //과목 이름
    private String professor; //교수명
    private String department; //개설 학과
    private String majorClassification; //이수 구분(주전공)
    private Double credit; //학점
    private String subjectTarget; //수강 대상

    /**시간에 따른 조회 기능 추가 예정*/

}
