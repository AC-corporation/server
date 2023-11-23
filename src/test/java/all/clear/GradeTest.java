package all.clear;


import all.clear.domain.User;
import all.clear.domain.grade.Grade;
import all.clear.domain.grade.SemesterGrade;
import all.clear.domain.grade.SemesterSubject;
import all.clear.dto.responseDto.GradeDto;
import all.clear.service.SemesterGradeService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GradeTest {
    @Autowired
    EntityManager em;

    @Autowired
    SemesterGradeService semesterGradeService;

    @Test
    public void 성적_조회(){
        //given
        User user = new User();
        Grade grade = new Grade();

        SemesterSubject subject1 = SemesterSubject.createSemesterSubject("데통네", "1.0");
        SemesterSubject subject2 = SemesterSubject.createSemesterSubject("윈프실", "1.5");
        SemesterGrade semestergrade1 = SemesterGrade.createSemesterGrade(grade, "1.25", subject1, subject2);

        SemesterSubject subject3 = SemesterSubject.createSemesterSubject("고컴수", "2.0");
        SemesterSubject subject4 = SemesterSubject.createSemesterSubject("생명", "2.5");
        SemesterGrade semestergrade2 = SemesterGrade.createSemesterGrade(grade, "2.25", subject3, subject4);

        grade.setUser(user);
        grade.setAverageGrade("1.75");
        grade.setTotalCredit(130L);
        grade.addSemesterGrade(semestergrade1);
        grade.addSemesterGrade(semestergrade2);

        //when
        /**리포지토리에서 조회하는 코드 추가 필요**/

        /**아래는 임시 코드**/
        

        //then
        /**조회한 성정 print**/
    }

}
