package all.clear.service;

import all.clear.domain.grade.SemesterGrade;
import all.clear.repository.SemesterGradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SemesterGradeService {
    private SemesterGradeRepository semesterGradeRepository;

    public SemesterGrade findOne(Long id){return semesterGradeRepository.findOne(id);}
}
