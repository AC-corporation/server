package allclear.service;

import allclear.crawl.CrawlSubjectInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.GetSubjectListRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
import allclear.dto.responseDto.subject.SubjectListResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import allclear.global.exception.GlobalExceptionHandler;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.subject.SubjectRepository;
import allclear.repository.subject.SubjectSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    @Autowired
    private final SubjectRepository subjectRepository;


    //과목 추가
    @Transactional
    public void createSubject(CreateSubjectRequestDto request){
        CrawlSubjectInfo subjectInfo = new CrawlSubjectInfo(
                request.getYear(),
                request.getSemester(),
                request.getUsaintId(),
                request.getUsaintPassword()
        );
        List<Subject> subjectList = subjectInfo.getSubjects();
        subjectRepository.saveAll(subjectList);
    }

    //과목 수정
    @Transactional
    public void updateSubject(UpdateSubjectRequestDto request){
        CrawlSubjectInfo subjectInfo = new CrawlSubjectInfo(
                request.getYear(),
                request.getSemester(),
                request.getUsaintId(),
                request.getUsaintPassword()
        );
        List<Subject> subjectList = subjectRepository.findAll();
        subjectList = subjectInfo.getSubjects();
    }

    //==과목 조회==//

    //단건 조회
    public SubjectResponseDto getSubject(Long id){
        Optional<Subject> subject = subjectRepository.findById(id);
        if (!subject.isPresent())
            throw new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS);
        return new SubjectResponseDto(subject.get());
    }

    //다건 조회
    public SubjectListResponseDto getSubjectList(GetSubjectListRequestDto request) {
        Specification<Subject> spec = new SubjectSpecification(request);
        List<Subject> subjectList = subjectRepository.findAll(spec);
        if (subjectList.isEmpty())
            throw new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS);
        return new SubjectListResponseDto(subjectList);
    }


    //과목 삭제
//    public void deleteSubject(Long id){
//        subjectRepository.deleteById(id);
//    }
}
