package allclear.service;

import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableSubject;
import allclear.dto.requestDto.AddCustomTimetableSubjectRequestDto;
import allclear.dto.requestDto.AddTimetableSubjectRequestDto;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.repository.SubjectRepository;
import allclear.repository.TimetableRepository;
import allclear.repository.TimetableSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimetableService {
    @Autowired
    private final TimetableRepository timetableRepository;
    @Autowired
    private final TimetableSubjectRepository timetableSubjectRepository;
    @Autowired
    private final SubjectRepository subjectRepository;


    public Timetable findOne(Long id) {
        return timetableRepository.findById(id).get();
    }

    //시간표 조회
    public TimetableResponseDto getTimetable(Long id) {
        return new TimetableResponseDto(findOne(id));
    }

    //시간표 삭제
    @Transactional
    public void deleteTimetable(Long id) {
        timetableRepository.deleteById(id);
    }

    //시간표 추가
    //시간표 업데이트
    //과목 수정
    //과목 조회

    //과목 삭제
    @Transactional
    public void deleteTimetableSubject(Long timetableSubjectId) {
        timetableSubjectRepository.deleteById(timetableSubjectId);
    }

    //과목 추가 - 실제 과목
    @Transactional
    public Long addTimetableSubject(Long timetableId, AddTimetableSubjectRequestDto request) {
        Timetable timetable = findOne(timetableId);
        Subject subject = subjectRepository.findById(request.getSubjectId()).get();

        //시간표 과목 생성해서 시간표에 추가
        TimetableSubject timetableSubject = TimetableSubject.createActualTimeTableSubject(subject);
        timetable.addTimetableSubject(timetableSubject);

        return timetableSubject.getTimetableSubjectId();
    }

    //과목 추가 - 커스텀 과목
    @Transactional
    public Long addTimetableSubject(Long timetableId, AddCustomTimetableSubjectRequestDto request) {
        Timetable timetable = findOne(timetableId);

        //시간표 과목 생성해서 시간표에 추가
        TimetableSubject timetableSubject = TimetableSubject.createCustomTimeTableSubject(
                request.getSubjectName(),
                request.getProfessor(),
                request.getClassInfoList()
        );
        timetable.addTimetableSubject(timetableSubject);

        return timetableSubject.getTimetableSubjectId();
    }
}
