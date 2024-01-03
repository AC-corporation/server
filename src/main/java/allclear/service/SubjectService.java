package allclear.service;

import allclear.repository.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {
    @Autowired
    private final SubjectRepository subjectRepository;



    //과목 추가
    //과목 삭제
    //과목 수정
    //과목 조회
}
