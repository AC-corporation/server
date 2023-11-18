package all.clear.service;

import all.clear.domain.subject.Subject;
import all.clear.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Transactional
    public void saveItem(Subject subject){
        subjectRepository.save(subject);
    }

    public Subject findOne(Long id){return subjectRepository.findOne(id);}
    public List<Subject> findSubjects(){return subjectRepository.findAll();}
}
