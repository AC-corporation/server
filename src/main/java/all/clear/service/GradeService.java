package all.clear.service;

import all.clear.domain.grade.Grade;
import all.clear.dto.responseDto.GradeDto;
import all.clear.repository.GradeRepository;
import all.clear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GradeService {

    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;

    /**
     * 성적 업데이트
     */
//    public Grade update(Long gradeId) {
//    }

    /**
     * 성적 조회
     */
//    public Grade findByUserId() {
//    }
}
