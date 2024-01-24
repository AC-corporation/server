package allclear.service.timetableGenerator;

import allclear.domain.member.Member;
import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableClassInfo;
import allclear.domain.timetable.TimetableSubject;
import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorClassInfo;
import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.dto.requestDto.timetable.AddCustomTimetableSubjectRequestDto;
import allclear.dto.requestDto.timetable.ClassInfoRequestDto;
import allclear.dto.requestDto.timetableGenerator.*;
import allclear.global.exception.GlobalExceptionHandler;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.member.MemberRepository;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorTimetableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimetableGeneratorManager {
    @Autowired
    private final TimetableGeneratorRepository timetableGeneratorRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final TimetableRepository timetableRepository;
    @Autowired
    private final TimetableGeneratorTimetableRepository timetableGeneratorTimetableRepository;
    @Autowired
    private final MemberRepository memberRepository;

    //시간표 생성기 조회
    private TimetableGenerator findById(Long userId) {
        return timetableGeneratorRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS));
    }

    /**
     * 시간표 생성기 생성
     */
    @Transactional
    public void createTimetableGenerator(Long userId, Step1RequestDto requestDto) {
        TimetableGenerator timetableGenerator = new TimetableGenerator();
        timetableGenerator.setId(userId);
        timetableGenerator.setTableYear(requestDto.getTableYear());
        timetableGenerator.setSemester(requestDto.getSemester());
        timetableGeneratorRepository.save(timetableGenerator);
    }

    /**
     * 시간표 생성기 과목 추가 - 커스텀 과목
     */
    @Transactional
    public void addCustomTimetableGeneratorSubjects(Long userId, Step2RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);
        for (AddCustomTimetableSubjectRequestDto customSubjectRequestDto : requestDto.getCustomTimetableSubjectRequestDtoList()) {

            //ClassInfo 리스트 초기화
            List<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();
            for (ClassInfoRequestDto classInfoRequestDto : customSubjectRequestDto.getClassInfoRequestDtoList()) {
                TimetableGeneratorClassInfo timetableGeneratorClassInfo = TimetableGeneratorClassInfo.createClassInfo(
                        classInfoRequestDto.getProfessor(),
                        classInfoRequestDto.getClassDay(),
                        classInfoRequestDto.getStartTime(),
                        classInfoRequestDto.getEndTime(),
                        classInfoRequestDto.getClassRoom()
                );
                timetableGeneratorClassInfoList.add(timetableGeneratorClassInfo);
            }

            //시간표 생성기 과목 생성해서 시간표 생성기에 추가
            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createCustomTimetableGeneratorSubject(
                    customSubjectRequestDto.getSubjectName(),
                    timetableGeneratorClassInfoList
            );
            timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);
        }
    }

    /**
     * 시간표 생성기 과목 추가 - 실제 과목
     */
    @Transactional
    public void addActualTimetableGeneratorSubjects(Long userId, Step3to6requestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);

        List<Subject> subjectList = subjectRepository.findAllById(requestDto.getSubjectIdList());
        for (Subject subject : subjectList) {
            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createActualTimetableGeneratorSubject(subject);
            timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);
        }
    }

    /**
     * 생성한 시간표 저장
     */
    @Transactional
    public void saveTimetable(Long userId, Step8RequestDto requestDto) {
        //생성할 시간표를 시간표 생성기에서 찾아오기
        TimetableGeneratorTimetable generatorTimetable = timetableGeneratorTimetableRepository.findById(requestDto.getTimetableId())
                .orElseThrow(() -> new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = findById(userId);

        //시간표 객체 생성
        Timetable timetable = Timetable.createTimetable(
                member,
                "새 시간표",
                timetableGenerator.getTableYear(),
                timetableGenerator.getSemester()
        );

        //시간표에 과목 추가
        for (TimetableGeneratorSubject generatorSubject : generatorTimetable.getTimetableGeneratorSubjects()) {
            TimetableSubject timetableSubject;

            if (generatorSubject.getSubject() == null) {
                //커스텀 과목 생성
                List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>();
                for (TimetableGeneratorClassInfo generatorClassInfo : generatorSubject.getClassInfoList()) {
                    timetableClassInfoList.add(TimetableClassInfo.createClassInfo(
                            generatorClassInfo.getProfessor(),
                            generatorClassInfo.getClassDay(),
                            generatorClassInfo.getStartTime(),
                            generatorClassInfo.getEndTime(),
                            generatorClassInfo.getClassRoom()
                    ));
                }
                timetableSubject = TimetableSubject.createCustomTimeTableSubject(
                        generatorSubject.getSubjectName(),
                        timetableClassInfoList
                );
            } else {
                //실제 과목 생성
                timetableSubject = TimetableSubject.createActualTimeTableSubject(generatorSubject.getSubject());
            }
            //생성한 과목 시간표에 추가
            timetable.addTimetableSubject(timetableSubject);
        }
        //DB에 시간표 저장
        timetableRepository.save(timetable);
    }
}
