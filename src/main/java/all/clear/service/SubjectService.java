package all.clear.service;

import all.clear.domain.Subject;
import all.clear.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public Subject findOne(Long id){return subjectRepository.findOne(id);}
    public List<Subject> findSubjects(){return subjectRepository.findAll();}
}
