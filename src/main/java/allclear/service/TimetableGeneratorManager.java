package allclear.service;

import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableClassInfo;
import allclear.domain.timetable.TimetableSubject;
import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorClassInfo;
import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.dto.requestDto.timetable.ClassInfoRequestDto;
import allclear.dto.requestDto.timetableGenerator.*;
import allclear.dto.responseDto.timetableGenerator.Step3to6ResponseDto;
import allclear.dto.responseDto.timetableGenerator.Step7ResponseDto;
import allclear.dto.responseDto.timetableGenerator.Step8ResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.member.MemberRepository;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.subject.SubjectSpecification;
import allclear.repository.timetable.TimetableRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorSubjectRepository;
import allclear.repository.timetableGenerator.TimetableGeneratorTimetableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TimetableGeneratorManager {
    private final TimetableGeneratorRepository tgRepository;
    private final SubjectRepository subjectRepository;
    private final TimetableRepository timetableRepository;
    private final TimetableGeneratorTimetableRepository tgTimetableRepository;
    private final MemberRepository memberRepository;
    private final TimetableGeneratorSubjectRepository tgSubjectRepository;


    //시간표 생성기 조회
    private TimetableGenerator findById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS))
                .getTimetableGenerator();
    }

    //==Step1==//

    /**
     * 시간표 생성기 초기화
     * Step1
     * Post
     */
    public void initTimetableGenerator(Long userId, Step1RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);
        timetableGenerator.getTimetableGeneratorTimetableList().clear();
        timetableGenerator.getTimetableGeneratorSubjectList().clear();
//        timetableGenerator.setTableYear(requestDto.getTableYear());
//        timetableGenerator.setSemester(requestDto.getSemester());
        timetableGenerator.initGenerator(requestDto.getTableYear(),requestDto.getSemester());

        tgTimetableRepository.deleteAll(timetableGenerator.getTimetableGeneratorTimetableList());
        tgSubjectRepository.deleteAll(timetableGenerator.getTimetableGeneratorSubjectList());
        tgRepository.save(timetableGenerator);
    }


    //==Step2==//

    /**
     * 시간표 생성기 과목 추가 - 커스텀 과목
     * Step2
     * Post
     */
    public Long addCustomTimetableGeneratorSubject(Long userId, Step2RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);
        ArrayList<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();

        for (ClassInfoRequestDto classInfoRequestDto : requestDto.getClassInfoRequestDtoList()) {
            timetableGeneratorClassInfoList.add(TimetableGeneratorClassInfo.builder()
                    .professor(classInfoRequestDto.getProfessor())
                    .classDay(classInfoRequestDto.getClassDay())
                    .startTime(classInfoRequestDto.getStartTime())
                    .endTime(classInfoRequestDto.getEndTime())
                    .classRoom(classInfoRequestDto.getClassRoom()).build()
            );
        }

        TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createCustomTimetableGeneratorSubject(
                requestDto.getSubjectName(),
                timetableGeneratorClassInfoList
        );

        timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);

        tgRepository.flush();
        return timetableGeneratorSubject.getId();
    }


    //==Step3==//

    /**
     * 전공 기초/필수 추천 (미완성)
     * Step3
     * Get
     */
    public Step3to6ResponseDto suggestMajorSubject(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        Requirement requirement = member.getRequirement();

        //수강한 과목 제외
        //입학년도 별 교과과정 조회

        //검색 조건 추가 필요
        List<Subject> subjectList = subjectRepository.findAll(SubjectSpecification.subjectFilter(
                SubjectSpecification.builder()
//                        .majorClassification(member.getMajor()) 소프트웨어 -> 소프트 이런식으로 바꿔야 함
                        .year(String.valueOf(member.getLevel()))
                        .build()
        ));

        //빠진 조건 필터링 추가 필요

        return new Step3to6ResponseDto(subjectList);
    }

    /**
     * 시간표 생성기 과목 추가 - 실제 과목
     * Step3~6
     * Post
     */
    public void addActualTimetableGeneratorSubjects(Long userId, Step3to6requestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);

        List<Subject> subjectList = subjectRepository.findAllById(requestDto.getSubjectIdList());
        for (Subject subject : subjectList) {
            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createActualTimetableGeneratorSubject(subject);
            timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);
        }
    }


    //==Step4==//

    /**
     * 교양 필수 추천 (미완성)
     * Step4
     * Get
     */
    public Step3to6ResponseDto suggestLiberalArtsSubject(Long userId) {
        return new Step3to6ResponseDto(null);
    }

    //==Step5==//

    /**
     * 전공 선택 추천 (미완성)
     * Step5
     * Get
     */
    public Step3to6ResponseDto suggestMajorElectiveSubject(Long userId) {
        return new Step3to6ResponseDto(null);
    }

    //==Step6==//

    /**
     * 전공 선택 추천 (미완성)
     * Step6
     * Get
     */
    public Step3to6ResponseDto suggestLiberalArtsElectiveSubject(Long userId) {
        return new Step3to6ResponseDto(null);
    }


    //==Step7==//

    /**
     * 선택한 과목 리스트 제공
     * Step7
     * get
     */
    public Step7ResponseDto getTimetableGeneratorSubjectList(Long userId) {
        List<TimetableGeneratorSubject> timetableGeneratorSubjectList = findById(userId)
                .getTimetableGeneratorSubjectList();
        if (timetableGeneratorSubjectList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new Step7ResponseDto(timetableGeneratorSubjectList);
    }

    /**
     * 필수 수강 과목 선택
     * Step7
     * Post
     */
    public void checkSelectedTimetableSubject(Step7RequestDto requestDto) {
        List<TimetableGeneratorSubject> tgSubjectList = tgSubjectRepository.findAllById(requestDto.getTimetableGeneratorSubjectIdList());

        for (TimetableGeneratorSubject tgSubject : tgSubjectList)
            tgSubject.setSelected(true);
    }

    /**
     * 시간표 생성 (미완성)
     * Step 7
     * Post
     */
    public void generateTimetable(Long userId) {
    }


    //==Step8==//

    /**
     * 시간표 불러오기
     * Step8
     * Get
     */
    public Step8ResponseDto getTimetableGeneratorTimetableList(Long userId) {
        TimetableGenerator timetableGenerator = findById(userId);
        return new Step8ResponseDto(timetableGenerator.getTimetableGeneratorTimetableList());
    }

    /**
     * 생성한 시간표 저장
     * Step8
     * Post
     */
    public void saveTimetable(Long userId, Step8RequestDto requestDto) {
        //생성할 시간표를 시간표 생성기에서 찾아오기
        TimetableGeneratorTimetable generatorTimetable = tgTimetableRepository.findById(requestDto.getTimetableGeneratorTimetableId())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = findById(userId);

        //시간표 객체 생성
//        Timetable timetable = Timetable.createTimetable(
//                member,
//                "새 시간표",
//                timetableGenerator.getTableYear(),
//                timetableGenerator.getSemester()
//        );

        Timetable timetable = Timetable.builder().member(member)
                .tableName("새 시간표")
                .tableYear(timetableGenerator.getTableYear())
                .semester(timetableGenerator.getSemester()).build();

        //시간표에 과목 추가
        for (TimetableGeneratorSubject generatorSubject : generatorTimetable.getTimetableGeneratorSubjectList()) {
            TimetableSubject timetableSubject;

            if (generatorSubject.getSubject() == null) {
                //커스텀 과목 생성
                List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>();
                for (TimetableGeneratorClassInfo generatorClassInfo : generatorSubject.getTimetableGeneratorClassInfoList()) {
                    timetableClassInfoList.add(
//                            TimetableClassInfo.createClassInfo(
//                            generatorClassInfo.getProfessor(),
//                            generatorClassInfo.getClassDay(),
//                            generatorClassInfo.getStartTime(),
//                            generatorClassInfo.getEndTime(),
//                            generatorClassInfo.getClassRoom()
//                    )
                            TimetableClassInfo.builder().professor(generatorClassInfo.getProfessor())
                                    .classDay(generatorClassInfo.getClassDay())
                                    .startTime(generatorClassInfo.getStartTime())
                                    .endTime(generatorClassInfo.getEndTime())
                                    .classRoom(generatorClassInfo.getClassRoom()).build()
                    );
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


    //==기타 메서드==//

    /**
     * 시간표 생성기 과목 삭제
     */
    public void deleteTimetableGeneratorSubject(Long timetableGeneratorSubjectId) {
        tgSubjectRepository.deleteById(timetableGeneratorSubjectId);
    }
}
