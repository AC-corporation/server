package all.clear.service;

import all.clear.crwal.CrwalMemberInfo;
import all.clear.domain.grade.Grade;
import all.clear.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    /**
     * 성적 업데이트
     */
    public void updateGradeInformation(Grade updatedGrade, CrwalMemberInfo userInfo) {
        // 업데이트할 Grade 엔터티를 가져오거나, 새로운 인스턴스를 생성하여 업데이트할 정보를 설정합니다.
        Grade existingGrade = gradeRepository.findById(updatedGrade.getGradeId()).orElse(null);

        if (existingGrade != null) {
            /** 업데이트할 정보를 설정합니다. **/
            // save 메서드를 사용하여 엔터티를 업데이트합니다.
            gradeRepository.save(existingGrade);
        } else {
            // 엔터티가 존재하지 않으면 예외처리 또는 다른 처리를 수행할 수 있습니다.
            // 여기에서는 간단히 메시지를 출력하도록 예제를 작성했습니다.
            System.out.println("Grade not found for updating.");
        }
    }

    /**
     * 성적 조회
     */
    public Grade findByMemberId(Long memberId) {
//        return gradeRepository.findByMemberId(memberId);
        return null;
    }
}
