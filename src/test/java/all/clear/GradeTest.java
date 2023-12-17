package all.clear;

import all.clear.domain.Member;
import all.clear.domain.grade.Grade;
import all.clear.domain.grade.SemesterGrade;
import all.clear.domain.grade.SemesterSubject;
import all.clear.repository.GradeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GradeTest {
    @Autowired
    EntityManager em;

    @Autowired
    GradeRepository gradeRepository;

    @Test
    public void 성적_조회(){
        //given
        Member member = new Member();
        Grade grade = new Grade();

        SemesterSubject subject1 = SemesterSubject.createSemesterSubject("데통네", "1.0");
        SemesterSubject subject2 = SemesterSubject.createSemesterSubject("윈프실", "1.5");
        SemesterGrade semestergrade1 = SemesterGrade.createSemesterGrade(grade, "1.25", subject1, subject2);

        SemesterSubject subject3 = SemesterSubject.createSemesterSubject("고컴수", "2.0");
        SemesterSubject subject4 = SemesterSubject.createSemesterSubject("생명", "2.5");
        SemesterGrade semestergrade2 = SemesterGrade.createSemesterGrade(grade, "2.25", subject3, subject4);

        grade.setMember(member);
        grade.setAverageGrade("1.75");
        grade.setTotalCredit(130L);
        grade.addSemesterGrade(semestergrade1);
        grade.addSemesterGrade(semestergrade2);

        em.persist(grade);

        //when
        Long memberId = member.getMemberId();

//        Grade sampleGrade = gradeRepository.findByMemberId(memberId);
        Grade sampleGrade = grade;

        //then
        System.out.println("gradeId = "+sampleGrade.getGradeId());
        System.out.println("averageGrade = "+sampleGrade.getAverageGrade());
        System.out.println("totalCredit = "+sampleGrade.getTotalCredit());
        System.out.println();
        System.out.println();
    }

}
