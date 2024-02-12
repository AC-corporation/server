package allclear.service;

import allclear.domain.member.Member;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableClassInfo;
import allclear.domain.timetable.TimetableSubject;
import allclear.dto.requestDto.timetable.CreateTimetableRequestDto;
import allclear.dto.requestDto.timetable.TimetableSubjectRequestDto;
import allclear.dto.requestDto.timetable.UpdateTimetableRequestDto;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.member.MemberRepository;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetable.TimetableSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final TimetableSubjectRepository timetableSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final MemberRepository memberRepository;


    /**
     * 시간표 추가
     * Post
     */
    @Transactional
    public Long createTimetable(Long memberId, CreateTimetableRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));
        Timetable timetable = Timetable
                .builder()
                .tableName(request.getTableName())
                .tableYear(request.getTableYear())
                .semester(request.getSemester())
                .timetableSubjectList(new ArrayList<>())
                .build();
        timetable.setMember(member);
        timetableRepository.save(timetable);
        return timetable.getTimetableId();
    }

    /**
     * 시간표 업데이트
     * Put
     */
    @Transactional
    public void updateTimetable(Long id, UpdateTimetableRequestDto request) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        timetableSubjectRepository.deleteAll(timetable.getTimetableSubjectList());
        timetable.getTimetableSubjectList().clear();
        timetableRepository.save(timetable);

        //시간표에 과목 추가
        for (TimetableSubjectRequestDto subjectRequestDto : request.getTimetableSubjectRequestDtoList()) {
            if (subjectRequestDto.getSubjectId() != null) {
                Subject subject = subjectRepository.findById(subjectRequestDto.getSubjectId())
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
                timetable.addTimetableSubject(TimetableSubject.createActualTimeTableSubject(subject));
            } else {
                timetable.addTimetableSubject(TimetableSubject.createCustomTimeTableSubject(
                        subjectRequestDto.getSubjectName(),
                        subjectRequestDto.getClassInfoRequestDtoList()
                                .stream()
                                .map(classInfoRequestDto -> TimetableClassInfo
                                        .builder()
                                        .professor(classInfoRequestDto.getProfessor())
                                        .classDay(classInfoRequestDto.getClassDay())
                                        .startTime(classInfoRequestDto.getStartTime())
                                        .endTime(classInfoRequestDto.getEndTime())
                                        .classRoom(classInfoRequestDto.getClassRoom())
                                        .build())
                                .collect(Collectors.toList())
                ));
            }
        }
        timetable.setTableName(request.getTableName());
        timetableRepository.save(timetable);
    }

    /**
     * 시간표 조회
     * Get
     */
    public TimetableResponseDto getTimetable(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        return new TimetableResponseDto(timetable);
    }

    /**
     * 시간표 삭제
     * Delete
     */
    @Transactional
    public void deleteTimetable(Long id) {
        timetableRepository.deleteById(id);
    }
}
