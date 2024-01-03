package allclear.service;

import allclear.domain.member.Member;
import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableSubject;
import allclear.dto.requestDto.timetable.*;
import allclear.dto.responseDto.timetable.ClassInfoDto;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.dto.responseDto.timetable.TimetableSubjectResponseDto;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.member.MemberRepository;
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetable.TimetableSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {
    @Autowired
    private final TimetableRepository timetableRepository;
    @Autowired
    private final TimetableSubjectRepository timetableSubjectRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final MemberRepository memberRepository;


    private Timetable findOne(Long id) {
        return timetableRepository.findById(id).get();
    }

    //시간표 추가
    @Transactional
    public Long createTimetable(Long memberId, CreateTimetableRequestDto request) {
        Member member = memberRepository.findById(memberId).get();

        Timetable timetable = Timetable.createTimetable(
                member,
                request.getTableName(),
                request.getTableYear(),
                request.getSemester(),
                subjectRepository.findAllById(request.getSubjectIdList())
                        .stream()
                        .map(TimetableSubject::createActualTimeTableSubject)
                        .collect(Collectors.toList())
        );
        timetableRepository.save(timetable);
        return timetable.getTimetableId();
    }

    @Transactional
    //시간표 업데이트
    public void updateTimetable(Long timetableId, UpdateTimetableRequestDto request) {
        Timetable timetable = findOne(timetableId);
        timetable.setTableName(request.getTableName());
        timetable.setTableYear(request.getTableYear());
        timetable.setSemester(request.getSemester());
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

        //ClassInfo 리스트 초기화
        List<ClassInfo> classInfoList = new ArrayList<>();
        for (ClassInfoDto classInfoDto: request.getClassInfoDtoListList()) {
            ClassInfo classInfo = ClassInfo.createClassInfo(
                    classInfoDto.getProfessor(),
                    classInfoDto.getClassTime(),
                    classInfoDto.getClassDate(),
                    classInfoDto.getClassRoom()
            );
            classInfoList.add(classInfo);
        }

        //시간표 과목 생성해서 시간표에 추가
        TimetableSubject timetableSubject = TimetableSubject.createCustomTimeTableSubject(
                request.getSubjectName(),
                classInfoList
        );
        timetable.addTimetableSubject(timetableSubject);

        return timetableSubject.getTimetableSubjectId();
    }

    //과목 수정
    @Transactional
    public void updateTimetableSubject(Long timetableSubjectId, UpdateTimetableSubjectRequestDto request) {
        TimetableSubject timetableSubject = timetableSubjectRepository.findById(timetableSubjectId).get();
        if (request.getSubjectName() != null)
            timetableSubject.setSubjectName(request.getSubjectName());
        if (request.getClassInfoDtoList() != null){
            //ClassInfo 리스트 초기화
            List<ClassInfo> classInfoList = new ArrayList<>();
            for (ClassInfoDto classInfoDto: request.getClassInfoDtoList()) {
                ClassInfo classInfo = ClassInfo.createClassInfo(
                        classInfoDto.getProfessor(),
                        classInfoDto.getClassTime(),
                        classInfoDto.getClassDate(),
                        classInfoDto.getClassRoom()
                );
                classInfoList.add(classInfo);
            }
            timetableSubject.setClassInfoList(classInfoList);
        }
    }

    //과목 조회
    public TimetableSubjectResponseDto getTimetableSubject(Long timetableSubjectId) {
        TimetableSubject timetableSubject = timetableSubjectRepository.findById(timetableSubjectId).get();
        return new TimetableSubjectResponseDto(timetableSubject);
    }

    //과목 삭제
    @Transactional
    public void deleteTimetableSubject(Long timetableSubjectId) {
        timetableSubjectRepository.deleteById(timetableSubjectId);
    }
}
