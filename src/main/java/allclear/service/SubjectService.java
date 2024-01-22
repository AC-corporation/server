package allclear.service;

import allclear.crawl.CrawlSubjectInfo;
import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
import allclear.dto.responseDto.subject.SubjectListResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.subject.ClassInfoRepository;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.subject.SubjectSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final ClassInfoRepository classInfoRepository;

    //과목 추가
    @Transactional
    public void createSubject(CreateSubjectRequestDto request) {
        CrawlSubjectInfo subjectInfo = new CrawlSubjectInfo(
                request.getYear(),
                request.getSemester(),
                request.getUsaintId(),
                request.getUsaintPassword()
        );
        List<Subject> subjectList = subjectInfo.getSubjects();
        subjectRepository.saveAll(subjectList);
    }

    //과목 업데이트
    @Transactional
    public void updateSubject(UpdateSubjectRequestDto request) {
        CrawlSubjectInfo subjectInfo = new CrawlSubjectInfo(
                request.getYear(),
                request.getSemester(),
                request.getUsaintId(),
                request.getUsaintPassword()
        );

        List<Subject> subjectList = subjectInfo.getSubjects();
        Subject subject;
        Subject foundSubject;
        for(int i = 0;i < subjectList.size();i++){
            subject = subjectList.get(i);
            foundSubject = subjectRepository.findById(subject.getSubjectId()).orElse(null);
            if (foundSubject == null){
                subjectRepository.save(subject);
            }
            else {
                foundSubject.setSubjectName(subject.getSubjectName());
                foundSubject.setMajorClassification(subject.getMajorClassification());
                foundSubject.setMultiMajorClassification(subject.getMultiMajorClassification());
                foundSubject.setEngineeringCertification(subject.getEngineeringCertification());
                foundSubject.setClassType(subject.getClassType());
                foundSubject.setCredit(subject.getCredit());
                foundSubject.setDesign(subject.getDesign());
                foundSubject.setSubjectTime(subject.getSubjectTime());
                foundSubject.setSubjectTarget(subject.getSubjectTarget());
                // classInfo 연관관계 삭제 및 DB 삭제
                List<ClassInfo> removeClassInfoList = foundSubject.getClassInfoList();
                Iterator<ClassInfo> iterator = removeClassInfoList.iterator();
                while (iterator.hasNext()){ // 연관관계 삭제
                    ClassInfo classInfo = iterator.next();
                    iterator.remove();
                    classInfo.setSubject(null);
                    classInfoRepository.deleteById(classInfo.getId()); // DB 삭제
                }
                foundSubject.setClassInfoList(subject.getClassInfoList()); // 업데이트 내용 DB 저장
            }
        }

    }

    //==과목 조회==//

    //단건 조회
    public SubjectResponseDto getSubject(Long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SubjectResponseDto(subject);
    }

    //전체 조회
    public SubjectListResponseDto getSubjectList() {
        List<Subject> subjectList = subjectRepository.findAll();
        if (subjectList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SubjectListResponseDto(subjectList);
    }

    //검색 조회
    public SubjectListResponseDto getSubjectSearch(SubjectListRequestDto request) {
        List<Subject> subjectList = subjectRepository.findAll(SubjectSpecification.subjectFilter(request));
        if (subjectList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SubjectListResponseDto(subjectList);
    }


    //과목 삭제
//    public void deleteSubject(Long id){
//        subjectRepository.deleteById(id);
//    }
}
