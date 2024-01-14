package allclear.dto.requestDto.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GetSubjectListRequestDto {
    private String subjectTarget; //수강 대상
    private String year; //수강 학년
    private String searchString; //문자열 검색
}
