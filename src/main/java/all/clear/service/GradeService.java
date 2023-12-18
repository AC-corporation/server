package all.clear.service;

import all.clear.domain.grade.Grade;
import all.clear.dto.responseDto.GradeResponseDto;
import all.clear.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    public void save(Grade grade){
        gradeRepository.save(grade);
    }

    public Grade findOne(Long id){
        return gradeRepository.findById(id).get();
    }

    public GradeResponseDto getGrade(Long memberId){
        return null;
    }
}
