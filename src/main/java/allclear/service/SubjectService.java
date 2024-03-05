package allclear.service;

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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@EnableCaching
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final ClassInfoRepository classInfoRepository;

    /**
     * 과목 생성(업데이트)
     */
    @Transactional
    public void initSubject(InitSubjectRequestDto request) {
        CrawlSubjectInfo subjectInfo = new CrawlSubjectInfo(
                request.getYear(),
                request.getSemester(),
                request.getUsaintId(),
                request.getUsaintPassword()
        );

        List<Subject> subjectList = subjectInfo.getSubjects();
        Subject subject;
        Subject foundSubject;
        for (int i = 0; i < subjectList.size(); i++) {
            subject = subjectList.get(i);
            foundSubject = subjectRepository.findById(subject.getSubjectId()).orElse(null);
            if (foundSubject == null) {
                subjectRepository.save(subject);
            }
            else
            {
                // subject 업데이트
                if (foundSubject.getSubjectName().equals(subject.getSubjectName())) // 동일 과목 이수 구분 추가
                    foundSubject.addClassification(subject.getMajorClassification(), subject.getMultiMajorClassification());
                else {
                    foundSubject.updateSubject(subject.getSubjectId(), subject.getSubjectName(), subject.getMajorClassification(),
                            subject.getMultiMajorClassification(), subject.getLiberalArtsClassification(),
                            subject.getEngineeringCertification(), subject.getClassType(), subject.getCredit(),
                            subject.getDesign(), subject.getSubjectTime(), subject.getSubjectTarget());

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
                }
                subjectRepository.save(foundSubject);
            }
            subjectRepository.flush();
        }
    }

    //==과목 조회==//

    /**
     * 단건 조회
     * Get
     */
    public SubjectResponseDto getSubject(Long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SubjectResponseDto(subject);
    }

    //전체 조회
    public SubjectListResponseDto getSubjectList(int page) {
        Pageable pageable = PageRequest.of(page, 30);

        Page<Subject> subjectPage = subjectRepository.findAll(pageable);
        if (subjectPage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        return new SubjectListResponseDto(subjectPage);
    }

    /**
     * 검색 조회
     * Get
     */
    @Transactional
    public SubjectListResponseDto getSubjectSearch(SubjectSearchRequestDto request, int page) {

        Pageable pageable = PageRequest.of(page, 30);

        Page<Subject> subjectPage = subjectRepository.search(request, pageable);
        if (subjectPage.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        return new SubjectListResponseDto(subjectPage);
    }

    /*
    과목 삭제
    public void deleteSubject(Long id){
        subjectRepository.deleteById(id);
    }
     */
}
