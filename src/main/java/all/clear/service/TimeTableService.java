package all.clear.service;

import all.clear.domain.subject.Subject;
import all.clear.domain.TimeTable;
import all.clear.domain.TimeTableSubject;
import all.clear.domain.User;
import all.clear.repository.SubjectRepository;
import all.clear.repository.TimeTableRepository;
import all.clear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;


    /**TimeTable**/
    public Long timeTable(Long userId, Long subjectId){
        User user = userRepository.findOne(userId);
        Subject subject = subjectRepository.findOne(subjectId);

        TimeTableSubject timeTableSubject = TimeTableSubject.createTimeTableSubject(subject);

        /**TableName, Year, Semester 정보 입력 관련 논의 필요**/
        TimeTable timeTable = TimeTable.createTimeTable(user, "", 1, 1, timeTableSubject);

        timeTableRepository.save(timeTable);
        return timeTable.getTimetableId();
    }

    //==삭제 메서드==//
//    @Transactional
//    public void removeTimeTable(Long timeTableId){
//        TimeTable timeTable = timeTableRepository.findOne(timeTableId);
//        // timeTable.remove();
//    }

}
