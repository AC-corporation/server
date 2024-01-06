package allclear.crawl;

import allclear.domain.member.Member;
import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.repository.subject.SubjectRepository;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

class CrawlSubjectTest {
    @Test
    public static void main(String[] args) {
        ArrayList<Subject> subjectList = new ArrayList<>();
        CrawlSubjectInfo crawlSubjectInfo = new CrawlSubjectInfo(2024, "1", "20203058", "Jjw112233$");
        subjectList = crawlSubjectInfo.getSubjects();
        for(int i=0;i<subjectList.size();i++){
            Subject subject = subjectList.get(i);
            List<ClassInfo> classInfoList = subject.getClassInfoList();
            System.out.println("====================");
            System.out.println(subject.getSubjectId()); // 과목번호
            System.out.println(subject.getSubjectName()); // 과목명
            System.out.println(subject.getDepartment()); // 개설학과
            System.out.println(subject.getMajorClassification()); // 주전공
            System.out.println(subject.getMultiMajorClassification()); // 다전공
            System.out.println(subject.getEngineeringCertification()); // 공학인증
            System.out.println(subject.getClassType()); // 분반
            System.out.println(subject.getSubjectTime()); // 시간
            System.out.println(subject.getCredit()); // 학점
            System.out.println(subject.getDesign()); // 설계
            for(int j=0;j<classInfoList.size();j++){
                ClassInfo classInfo = classInfoList.get(j);
                System.out.println(classInfo.getClassDate()); // 요일
                System.out.println(classInfo.getClassTime()); // 시간
                System.out.println(classInfo.getClassRoom()); // 강의실
                System.out.println(classInfo.getProfessor()); // 교수명
            }
        }

    }
}
