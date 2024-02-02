package allclear.service;

import allclear.domain.member.Member;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableClassInfo;
import allclear.domain.timetable.TimetableSubject;
import allclear.dto.requestDto.timetable.*;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.dto.responseDto.timetable.TimetableSubjectResponseDto;
import allclear.repository.member.MemberRepository;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetable.TimetableSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final TimetableSubjectRepository timetableSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;


    private Timetable findOne(Long id) {
        return timetableRepository.findById(id).orElse(null);
    }

    //시간표 추가
    @Transactional
    public Long createTimetable(Long memberId, CreateTimetableRequestDto request) {
        Member member = memberRepository.findById(memberId).orElse(null);

//        Timetable timetable = Timetable.createTimetable(
//                member,
//                request.getTableName(),
//                request.getTableYear(),
//                request.getSemester()
//        );
        Timetable timetable = Timetable.builder().member(member)
                .tableName(request.getTableName())
                .tableYear(request.getTableYear())
                .semester(request.getSemester()).build();
        timetableRepository.save(timetable);
        return timetable.getTimetableId();
    }

    @Transactional
    //시간표 업데이트
    public void updateTimetable(Long timetableId, UpdateTimetableRequestDto request) {
        Timetable timetable = findOne(timetableId);
//        timetable.setTableName(request.getTableName());
//        timetable.setTableYear(request.getTableYear());
//        timetable.setSemester(request.getSemester());
        timetable.updateTimetable(request.getTableName(), request.getTableYear(), request.getSemester());
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



    //시간표 과목 추가 - 실제 과목
    @Transactional
    public Long addTimetableSubject(Long timetableId, AddTimetableSubjectRequestDto request) {
        Timetable timetable = findOne(timetableId);
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);

        //시간표 과목 생성해서 시간표에 추가
        TimetableSubject timetableSubject = TimetableSubject.createActualTimeTableSubject(subject);
        timetable.addTimetableSubject(timetableSubject);

        //Id 생성
        timetableRepository.flush();

        return timetableSubject.getTimetableSubjectId();
    }

    //시간표 과목 추가 - 커스텀 과목
    @Transactional
    public Long addTimetableSubject(Long timetableId, AddCustomTimetableSubjectRequestDto request) {
        Timetable timetable = findOne(timetableId);

        //ClassInfo 리스트 초기화
        List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>();
        for (ClassInfoRequestDto classInfoRequestDto : request.getClassInfoRequestDtoList()) {
//            TimetableClassInfo timetableClassInfo = TimetableClassInfo.createClassInfo(
//                    classInfoRequestDto.getProfessor(),
//                    classInfoRequestDto.getClassDay(),
//                    classInfoRequestDto.getStartTime(),
//                    classInfoRequestDto.getEndTime(),
//                    classInfoRequestDto.getClassRoom()
//            );
            TimetableClassInfo timetableClassInfo = TimetableClassInfo.builder().professor(classInfoRequestDto.getProfessor())
                    .classDay(classInfoRequestDto.getClassDay())
                    .startTime(classInfoRequestDto.getStartTime())
                    .endTime(classInfoRequestDto.getEndTime())
                    .classRoom(classInfoRequestDto.getClassRoom()).build();
            timetableClassInfoList.add(timetableClassInfo);
        }

        //시간표 과목 생성해서 시간표에 추가
        TimetableSubject timetableSubject = TimetableSubject.createCustomTimeTableSubject(
                request.getSubjectName(),
                timetableClassInfoList
        );
        timetable.addTimetableSubject(timetableSubject);

        //Id 생성
        timetableRepository.flush();

        return timetableSubject.getTimetableSubjectId();
    }

    //시간표 과목 수정
    @Transactional
    public void updateTimetableSubject(Long timetableSubjectId, UpdateTimetableSubjectRequestDto request) {
        TimetableSubject timetableSubject = timetableSubjectRepository.findById(timetableSubjectId).orElse(null);
        assert timetableSubject != null;
        if (request.getSubjectName() != null)
            timetableSubject.setSubjectName(request.getSubjectName());
        if (request.getClassInfoRequestDtoList() != null){
            //ClassInfo 리스트 초기화
            List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>();
            for (ClassInfoRequestDto classInfoRequestDto : request.getClassInfoRequestDtoList()) {
//                TimetableClassInfo timetableClassInfo = TimetableClassInfo.createClassInfo(
//                        classInfoRequestDto.getProfessor(),
//                        classInfoRequestDto.getClassDay(),
//                        classInfoRequestDto.getStartTime(),
//                        classInfoRequestDto.getEndTime(),
//                        classInfoRequestDto.getClassRoom()
//                );
                TimetableClassInfo timetableClassInfo =TimetableClassInfo.builder()
                        .professor(classInfoRequestDto.getProfessor())
                        .classDay(classInfoRequestDto.getClassDay())
                        .startTime(classInfoRequestDto.getStartTime())
                        .endTime(classInfoRequestDto.getEndTime())
                        .classRoom(classInfoRequestDto.getClassRoom()).build();
                timetableClassInfoList.add(timetableClassInfo);
            }
            timetableSubject.setTimetableClassInfoList(timetableClassInfoList);
        }
    }

    //시간표 과목 조회
    public TimetableSubjectResponseDto getTimetableSubject(Long timetableSubjectId) {
        TimetableSubject timetableSubject = timetableSubjectRepository.findById(timetableSubjectId).orElse(null);
        assert timetableSubject != null;
        return new TimetableSubjectResponseDto(timetableSubject);
    }

    //시간표 과목 삭제
    @Transactional
    public void deleteTimetableSubject(Long timetableSubjectId) {
        timetableSubjectRepository.deleteById(timetableSubjectId);
    }
}
