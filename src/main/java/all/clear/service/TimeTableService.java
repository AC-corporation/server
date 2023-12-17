package all.clear.service;

import all.clear.domain.subject.Subject;
import all.clear.domain.TimeTable;
import all.clear.domain.TimeTableSubject;
import all.clear.domain.Member;
import all.clear.repository.SubjectRepository;
import all.clear.repository.TimeTableRepository;
import all.clear.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;


    /**TimeTable**/
    public Long timeTable(Long memberId, Long subjectId){
        Member member = memberRepository.findOne(memberId);
        Subject subject = subjectRepository.findOne(subjectId);

        TimeTableSubject timeTableSubject = TimeTableSubject.createTimeTableSubject(subject);

        /**TableName, Year, Semester 정보 입력 관련 논의 필요**/
        TimeTable timeTable = TimeTable.createTimeTable(member, "", 1, 1, timeTableSubject);

        timeTableRepository.save(timeTable);
        return timeTable.getTimetableId();
    }

    //==삭제 메서드==//
    @Transactional
    public void removeTimeTable(Long timeTableId){
        timeTableRepository.delete(timeTableId);
    }

}
