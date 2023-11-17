package all.clear.service;

import all.clear.domain.RequirementComponent;
import all.clear.repository.RequirementComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementComponentService {

    private final RequirementComponentRepository requirementComponentRepository;

    /**
     * 크롤링해온 졸업요건 행을 DB에 추가
     */
    public Long saveRequirementComponent(RequirementComponent requirementComponent){
        requirementComponentRepository.save(requirementComponent);
        return requirementComponent.getRequirementComponentId();
    }
}
