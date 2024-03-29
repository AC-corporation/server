package allclear;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import allclear.service.SubjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SubjectServiceTest {
    @Autowired private SubjectService subjectService;

    @Test
    public void 초기화() {
        /*
        //given
        InitSubjectRequestDto requestDto = new InitSubjectRequestDto(
                2023, "1", "20223168", "todo!9844"
        );

        //when
        subjectService.initSubject(requestDto);

        //then
        SubjectPageResponseDto responseDto = subjectService.getSubjectSearch(new SubjectSearchRequestDto(), 0);

        for (SubjectResponseDto subjectResponseDto : responseDto.getSubjectResponseDtoList()) {
            System.out.println(subjectResponseDto.getSubjectId());
            System.out.println(subjectResponseDto.getClassType());
            System.out.println(subjectResponseDto.getSubjectTarget());
            System.out.println(subjectResponseDto.getSubjectTime());
            System.out.println(subjectResponseDto.getCredit());
            System.out.println(subjectResponseDto.getMajorClassification());
            System.out.println();
        }
        */

    }
}
