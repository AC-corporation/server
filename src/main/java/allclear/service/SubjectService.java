package allclear.service;

import allclear.crawl.CrawlSubjectInfo;
import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.CreateSubjectRequestDto;
import allclear.dto.requestDto.subject.SubjectListRequestDto;
import allclear.dto.requestDto.subject.UpdateSubjectRequestDto;
import allclear.dto.responseDto.subject.SubjectListResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import allclear.repository.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    @Autowired
    private final SubjectRepository subjectRepository;


    //과목 추가
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
        Subject subject = subjectRepository.findById(id).get();
        return new SubjectResponseDto(subject);
    }

    //다건 조회
//    public SubjectListResponseDto getSubjectByDepartment(SubjectListRequestDto request) {
//        List<Subject> subjectList;
//        return new SubjectListResponseDto(subjectList);
//    }


    //과목 삭제
    public void deleteSubject(Long id){
        subjectRepository.deleteById(id);
    }
}
