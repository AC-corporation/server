package allclear.service.timetableGenerator;

import allclear.domain.member.Member;
import allclear.domain.requirement.RequirementComponent;
import allclear.domain.subject.Subject;
import allclear.domain.timetable.Timetable;
import allclear.domain.timetable.TimetableClassInfo;
import allclear.domain.timetable.TimetableSubject;
import allclear.domain.timetableGenerator.*;
import allclear.dto.requestDto.timetable.ClassInfoRequestDto;
import allclear.dto.requestDto.timetable.TimetableSubjectRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static allclear.service.timetableGenerator.TimetableGeneratorManager.generateTimetables;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TimetableGeneratorService {
    private final TimetableGeneratorRepository tgRepository;
    private final SubjectRepository subjectRepository;
    private final TimetableRepository timetableRepository;
    private final TimetableGeneratorTimetableRepository tgTimetableRepository;
    private final MemberRepository memberRepository;
    private final TimetableGeneratorSubjectRepository tgSubjectRepository;


    //시간표 생성기 조회
    private TimetableGenerator findById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND))
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
        tgTimetableRepository.deleteAll(timetableGenerator.getTimetableGeneratorTimetableList());
        tgSubjectRepository.deleteAll(timetableGenerator.getTimetableGeneratorSubjectList());
        timetableGenerator.getTimetableGeneratorTimetableList().clear();
        timetableGenerator.getTimetableGeneratorSubjectList().clear();
        timetableGenerator.initGenerator(requestDto.getTableYear(), requestDto.getSemester());
        tgRepository.save(timetableGenerator);
    }


    //==Step2==//

    /**
     * 시간표 생성기 과목 추가 - 커스텀 과목
     * Step2
     * Post
     */
    public void addCustomTimetableGeneratorSubjects(Long userId, Step2RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);
        List<TimetableGeneratorSubject> timetableGeneratorSubjectList = new ArrayList<>();
        for (TimetableSubjectRequestDto timetableSubjectRequestDto : requestDto.getTimetableSubjectRequestDtoList()) {
            List<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();

            for (ClassInfoRequestDto classInfoRequestDto : timetableSubjectRequestDto.getClassInfoRequestDtoList()) {
                timetableGeneratorClassInfoList.add(TimetableGeneratorClassInfo
                        .builder()
                        .professor(classInfoRequestDto.getProfessor())
                        .classDay(classInfoRequestDto.getClassDay())
                        .startTime(classInfoRequestDto.getStartTime())
                        .endTime(classInfoRequestDto.getEndTime())
                        .classRoom(classInfoRequestDto.getClassRoom())
                        .build()
                );
            }

            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createCustomTimetableGeneratorSubject(
                    timetableSubjectRequestDto.getSubjectName(),
                    timetableGeneratorClassInfoList
            );

            timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);
        }
        tgSubjectRepository.saveAll(timetableGeneratorSubjectList);
    }


    //==Step3==//

    /**
     * 전공 기초/필수 추천
     * Step3
     * Get
     */
    public Step3to6ResponseDto suggestMajorSubject(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = member.getTimetableGenerator();

        //조회할 과목 학년 저장
        Integer memberLevel = member.getLevel();
        if (LocalDate.now().getMonthValue() < 3 && member.getSemester() == 2)
            memberLevel++;
        //조회할 과목 전공 저장
        String major = majorConvertor(member.getMajor());


        //학과 전공 기초/필수 과목 조회
        List<Subject> subjectList = new ArrayList<>(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                SubjectSpecification.builder()
                        .category1("전기")
                        .category2("전필")
                        .majorClassification(major)
                        .year(String.valueOf(memberLevel))
                        .build()
        )));

        //입학년도 별 교과과정 조회
        for (Long subjectId : timetableGenerator.getCurriculumSubjectIdList())
            subjectList.addAll(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                    SubjectSpecification.builder()
                            .category1("전기")
                            .category2("전필")
                            .subjectId(subjectId)
                            .year(String.valueOf(memberLevel))
                            .build()
            )));

        //불필요 과목 삭제
        removeUnnecessarySubject(subjectList, timetableGenerator.getPrevSubjectIdList());
        //과목 정렬
        sortSubject(subjectList, major, memberLevel);
        //졸업요건 조회
        List<RequirementComponent> requirementComponentList = member.getRequirement().getRequirementComponentList()
                .stream()
                .filter(requirementComponent -> (requirementComponent.getRequirementCategory().equals("전공기초")
                        || requirementComponent.getRequirementCategory().equals("전공"))
                        && !requirementComponent.getRequirementArgument().contains("전선")).toList();
        return new Step3to6ResponseDto(requirementComponentList, subjectList);
    }

    /**
     * 시간표 생성기 과목 추가 - 실제 과목
     * Step3~7
     * Post
     */
    public void addActualTimetableGeneratorSubjects(Long userId, Step3to7RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);

        //중복 선택된 과목 제외
        List<Long> selectedSubjectId = timetableGenerator.getTimetableGeneratorSubjectList()
                .stream()
                .filter(timetableGeneratorSubject -> timetableGeneratorSubject.getSubject() != null)
                .map(timetableGeneratorSubject -> timetableGeneratorSubject.getSubject().getSubjectId())
                .toList();
        List<Subject> subjectList = subjectRepository.findAllById(requestDto.getSubjectIdList())
                .stream()
                .filter(subject -> !selectedSubjectId.contains(subject.getSubjectId()))
                .toList();

        boolean isMajor;
        for (Subject subject : subjectList) {
            isMajor = subject.getMajorClassification().contains(majorConvertor(timetableGenerator.getMember().getMajor()))
                    && (subject.getMajorClassification().contains("전기")
                        || subject.getMajorClassification().contains("전필")
                        || subject.getMajorClassification().contains("전선"));
            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createActualTimetableGeneratorSubject(subject, isMajor);
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
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = member.getTimetableGenerator();

        //조회할 과목 학년 저장
        Integer memberLevel = member.getLevel();
        if (LocalDate.now().getMonthValue() < 3 && member.getSemester() == 2)
            memberLevel++;
        //조회할 과목 전공 저장
        String major = majorConvertor(member.getMajor());


        //교양 필수 과목 조회
        List<Subject> subjectList = new ArrayList<>(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                SubjectSpecification.builder()
                        .majorClassification("교필")
                        .year(String.valueOf(memberLevel))
                        .build()
        )));

        //불필요 과목 삭제
        removeUnnecessarySubject(subjectList, timetableGenerator.getPrevSubjectIdList());
        //과목 정렬
        sortSubject(subjectList, major, memberLevel);
        //졸업요건 조회
        List<RequirementComponent> requirementComponentList = member.getRequirement().getRequirementComponentList()
                .stream()
                .filter(requirementComponent -> requirementComponent.getRequirementCategory().equals("교양필수")).toList();
        return new Step3to6ResponseDto(requirementComponentList, subjectList);
    }


    //==Step5==//

    /**
     * 전공 선택 추천
     * Step5
     * Get
     */
    public Step3to6ResponseDto suggestMajorElectiveSubject(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = member.getTimetableGenerator();

        //조회할 과목 학년 저장
        Integer memberLevel = member.getLevel();
        if (LocalDate.now().getMonthValue() < 3 && member.getSemester() == 2)
            memberLevel++;
        //조회할 과목 전공 저장
        String major = majorConvertor(member.getMajor());


        //학과 전공 선택 과목 조회
        List<Subject> subjectList = new ArrayList<>(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                SubjectSpecification.builder()
                        .category1("전선")
                        .category2("전필")
                        .majorClassification(major)
                        .year(String.valueOf(memberLevel))
                        .build()
        )));

        //입학년도 별 교과과정 조회
        for (Long subjectId : timetableGenerator.getCurriculumSubjectIdList())
            subjectList.addAll(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                    SubjectSpecification.builder()
                            .category1("전선")
                            .category2("전필")
                            .subjectId(subjectId)
                            .year(String.valueOf(memberLevel))
                            .build()
            )));

        //불필요 과목 삭제
        removeUnnecessarySubject(subjectList, timetableGenerator.getPrevSubjectIdList());
        //과목 정렬
        sortSubject(subjectList, major, memberLevel);
        //졸업요건 조회
        List<RequirementComponent> requirementComponentList = member.getRequirement().getRequirementComponentList()
                .stream()
                .filter(requirementComponent -> requirementComponent.getRequirementArgument().contains("전선")).toList();

        return new Step3to6ResponseDto(requirementComponentList, subjectList);
    }


    //==Step6==//

    /**
     * 교양 선택 추천 (미완성)
     * Step6
     * Get
     */
    public Step3to6ResponseDto suggestLiberalArtsElectiveSubject(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        TimetableGenerator timetableGenerator = member.getTimetableGenerator();

        //조회할 과목 학년 저장
        Integer memberLevel = member.getLevel();
        if (LocalDate.now().getMonthValue() < 3 && member.getSemester() == 2)
            memberLevel++;
        //조회할 과목 전공 저장
        String major = majorConvertor(member.getMajor());


        //교양 선택 과목 조회
        List<Subject> subjectList = new ArrayList<>(subjectRepository.findAll(SubjectSpecification.subjectFilter(
                SubjectSpecification.builder()
                        .majorClassification("교선")
                        .year(String.valueOf(memberLevel))
                        .build()
        )));

        //불필요 과목 삭제
        removeUnnecessarySubject(subjectList, timetableGenerator.getPrevSubjectIdList());
        //과목 정렬
        sortSubject(subjectList, major, memberLevel);
        //졸업요건 조회
        List<RequirementComponent> requirementComponentList = member.getRequirement().getRequirementComponentList()
                .stream()
                .filter(requirementComponent -> requirementComponent.getRequirementCategory().equals("교양선택")).toList();
        return new Step3to6ResponseDto(requirementComponentList, subjectList);
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
        List<TimetableGeneratorSubject> tgSubjectList = tgSubjectRepository.findAll();
        //잘못된 수강 학점 선택 예외처리
        if (requestDto.getMinCredit() > requestDto.getMaxCredit()
            || requestDto.getMinMajorCredit() > requestDto.getMaxMajorCredit())
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
        if (requestDto.getTimetableGeneratorSubjectIdList().size() < 1)
            throw new GlobalException(GlobalErrorCode._UNAVAILABLE_SELECTED_SUBJECT_NUMBER);
        for (TimetableGeneratorSubject tgSubject : tgSubjectList) {
            tgSubject.setSelected(false);
            if (requestDto.getTimetableGeneratorSubjectIdList().contains(tgSubject.getId())) {
                tgSubject.setSelected(true);
            }
        }
    }

    /**
     * 시간표 생성 (미완성)
     * Step 7
     * Post
     */
    public void generateTimetableList(Long userId, Step7RequestDto requestDto) {
        TimetableGenerator timetableGenerator = findById(userId);
        //시간표 생성
        List<TimetableGeneratorTimetable> newTGTimetableList = generateTimetables(timetableGenerator, requestDto);
        //생성기 시간표 초기화 후 생성한 시간표 넣기
        tgTimetableRepository.deleteAll(timetableGenerator.getTimetableGeneratorTimetableList());
        timetableGenerator.getTimetableGeneratorTimetableList().clear();
        tgRepository.save(timetableGenerator);
        for (TimetableGeneratorTimetable tgTimetable : newTGTimetableList)
            timetableGenerator.addTimetableGeneratorTimetable(tgTimetable);
        tgTimetableRepository.saveAll(newTGTimetableList);
    }


    //==Step8==//

    /**
     * 시간표 불러오기
     * Step8
     * Get
     */
    public Step8ResponseDto getTimetableGeneratorTimetableList(Long userId, int page) {
        TimetableGenerator timetableGenerator = findById(userId);
        Pageable pageable = PageRequest.of(page, 20);

        Page<TimetableGeneratorTimetable> timetablePage = tgTimetableRepository.findAllByTimetableGenerator(
                timetableGenerator, pageable
        );

        if (timetablePage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new Step8ResponseDto(timetablePage);
    }

    /**
     * 생성한 시간표 저장
     * Step8
     * Post
     */
    public Long saveTimetable(Long userId, Step8RequestDto requestDto) {
        //생성할 시간표를 시간표 생성기에서 찾아오기
        TimetableGeneratorTimetable generatorTimetable = tgTimetableRepository.findById(requestDto.getTimetableGeneratorTimetableId())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND));
        TimetableGenerator timetableGenerator = findById(userId);

        //시간표 객체 생성
        Timetable timetable = Timetable
                .builder()
                .tableName("새 시간표")
                .timetableSubjectList(new ArrayList<>())
                .tableYear(timetableGenerator.getTableYear())
                .semester(timetableGenerator.getSemester())
                .build();
        timetable.setMember(member);

        //시간표에 과목 추가
        for (TimetableGeneratorSubject generatorSubject : generatorTimetable.getTimetableGeneratorTimetableSubjectList()
                .stream()
                .map(TimetableGeneratorTimetableSubject::getTimetableGeneratorSubject)
                .toList()) {
            TimetableSubject timetableSubject;

            if (generatorSubject.getSubject() == null) {
                //커스텀 과목 생성
                List<TimetableClassInfo> timetableClassInfoList = new ArrayList<>();
                for (TimetableGeneratorClassInfo generatorClassInfo : generatorSubject.getTimetableGeneratorClassInfoList()) {
                    timetableClassInfoList.add(
                            TimetableClassInfo.
                                    builder()
                                    .professor(generatorClassInfo.getProfessor())
                                    .classDay(generatorClassInfo.getClassDay())
                                    .startTime(generatorClassInfo.getStartTime())
                                    .endTime(generatorClassInfo.getEndTime())
                                    .classRoom(generatorClassInfo.getClassRoom())
                                    .build()
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
        return timetable.getTimetableId();
    }


    //==기타 메서드==//

    /**
     * 시간표 생성기 과목 삭제
     */
    public void deleteTimetableGeneratorSubject(Long timetableGeneratorSubjectId) {
        tgSubjectRepository.findById(timetableGeneratorSubjectId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        tgSubjectRepository.deleteById(timetableGeneratorSubjectId);
    }


    /**
     * utils
     */

    //전공 문자열 변환
    private String majorConvertor(String major) {
        //IT 대학
        return major.contains("컴퓨터") ? "컴퓨터"
                : major.contains("소프트") ? "소프트"
                : major.contains("AI융합") ? "AI융합"
                : major.contains("글로벌미디어") ? "글로벌미디어"
                : major.contains("글로벌미디어") ? "글로벌미디어"
                : major.contains("미디어경영") ? "미디어경영"
                : major.contains("정보보호") ? "정보보호"
                : major.contains("전자정보공학부 it융합") ? "IT융합"
                : major.contains("전자정보공학부 전자공학") ? "전자공학"
                : major.substring(0, major.length() - 2);
    }

    //중복 과목 및 수강한 과목 삭제
    private void removeUnnecessarySubject(List<Subject> subjectList, List<Long> prevSubjectIdList) {
        //수강한 과목 제외
        subjectList.removeIf(subject -> prevSubjectIdList.contains(subject.getSubjectId() / 100));
        //중복 과목 제외
        Set<Long> uniqueSubjectIds = new HashSet<>();
        List<Subject> uniqueSubjects = new ArrayList<>();
        for (Subject subject : subjectList)
            if (uniqueSubjectIds.add(subject.getSubjectId()))
                uniqueSubjects.add(subject);
        subjectList.clear();
        subjectList.addAll(uniqueSubjects);
    }

    //유저 학년의 과목 앞쪽으로 정렬
    private void sortSubject(List<Subject> subjectList, String major, int level) {
        subjectList.sort(Comparator
                .comparing((Subject o) -> o.getSubjectTarget().contains(major) ? 0 : 1)
                .thenComparing(o -> o.getSubjectTarget().contains(String.valueOf(level)) ? 0 : 1));
    }
}
