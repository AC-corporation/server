package allclear.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import allclear.crawl.subject.CrawlSubjectInfo;
import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.InitSubjectRequestDto;
import allclear.dto.requestDto.subject.SubjectSearchRequestDto;
import allclear.dto.responseDto.subject.SubjectListResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.subject.ClassInfoRepository;
import allclear.repository.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;

@Service
@EnableCaching
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ClassInfoRepository classInfoRepository;

    /** 과목 생성(업데이트) */
    @Transactional
    public void initSubject(InitSubjectRequestDto request) {
        CrawlSubjectInfo subjectInfo =
                new CrawlSubjectInfo(
                        request.getYear(),
                        request.getSemester(),
                        request.getUsaintId(),
                        request.getUsaintPassword());

        List<Subject> subjectList = subjectInfo.getSubjects();
        Set<Long> subjectSet = new HashSet<>(); // 중복 크롤링된 과목 여부 확인 set
        Subject subject; // 크롤링이 새로된 과목
        Subject foundSubject; // 원래 DB에 존재하던 과목
        for (int i = 0; i < subjectList.size(); i++) {
            subject = subjectList.get(i);
            foundSubject = subjectRepository.findById(subject.getSubjectId()).orElse(null);
            if (foundSubject == null) { // DB에 원래 존재하지 않던 과목인 경우, 바로 DB에 save
                subjectRepository.save(subject);
                subjectSet.add(subject.getSubjectId()); // set에 subjectId 저장
            } else {
                // subject 업데이트
                if (subjectSet.contains(subject.getSubjectId())) { // 중복 크롤링 된 과목일 경우, 이수구분만 추가
                    foundSubject.addClassification(
                            subject.getMajorClassification(),
                            subject.getMultiMajorClassification());
                } else {
                    foundSubject.updateSubject(
                            subject.getSubjectId(),
                            subject.getSubjectName(),
                            subject.getMajorClassification(),
                            subject.getMultiMajorClassification(),
                            subject.getLiberalArtsClassification(),
                            subject.getEngineeringCertification(),
                            subject.getClassType(),
                            subject.getCredit(),
                            subject.getDesign(),
                            subject.getSubjectTime(),
                            subject.getSubjectTarget());

                    // classInfo 연관관계 삭제 및 DB 삭제
                    List<ClassInfo> removeClassInfoList = foundSubject.getClassInfoList();
                    for (ClassInfo removeClassInfo : removeClassInfoList) {
                        classInfoRepository.delete(removeClassInfo); // 삭제
                    }
                    classInfoRepository.flush(); // 즉시 DB 반영
                    removeClassInfoList.clear();

                    // classInfo 업데이트
                    List<ClassInfo> newClassInfoList = subject.getClassInfoList();
                    for (ClassInfo classInfo : newClassInfoList) {
                        classInfo.setSubject(foundSubject);
                    }
                    subjectSet.add(subject.getSubjectId()); // set에 subjectId 저장
                }
                subjectRepository.save(foundSubject);
            }
            subjectRepository.flush();
        }
    }

    // ==과목 조회==//

    /** 단건 조회 Get */
    public SubjectResponseDto getSubject(Long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null) throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SubjectResponseDto(subject);
    }

    // 전체 조회
    public SubjectListResponseDto getSubjectList(int page) {
        Pageable pageable = PageRequest.of(page, 30);

        Page<Subject> subjectPage = subjectRepository.findAll(pageable);
        if (subjectPage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        return new SubjectListResponseDto(subjectPage);
    }

    /** 검색 조회 Get */
    @Transactional
    public SubjectListResponseDto getSubjectSearch(SubjectSearchRequestDto request, int page) {

        Pageable pageable = PageRequest.of(page, 30);

        Page<Subject> subjectPage = subjectRepository.search(request, pageable);
        if (subjectPage.isEmpty()) throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        return new SubjectListResponseDto(subjectPage);
    }

    /*
    과목 삭제
    public void deleteSubject(Long id){
        subjectRepository.deleteById(id);
    }
     */
}
