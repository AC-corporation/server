package allclear;

import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.dto.responseDto.subject.SubjectListResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import allclear.service.SubjectService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SubjectServiceTest {
    @Autowired
    private SubjectService subjectService;

    @Test
    public void 초기화() {
        //given
        CreateSubjectRequestDto requestDto = new CreateSubjectRequestDto(
                2023, "1", "20223168", "todo!9844"
        );

        //when
        subjectService.createSubject(requestDto);

        //then
        SubjectListResponseDto responseDto = subjectService.getSubjectList(new SubjectListRequestDto());

        for (SubjectResponseDto subjectResponseDto : responseDto.getSubjectResponseDtoList()) {
            System.out.println(subjectResponseDto.getSubjectId());
            System.out.println(subjectResponseDto.getClassType());
            System.out.println(subjectResponseDto.getSubjectTarget());
            System.out.println(subjectResponseDto.getSubjectTime());
            System.out.println(subjectResponseDto.getCredit());
            System.out.println(subjectResponseDto.getMajorClassification());
            System.out.println();
        }
    }
}
