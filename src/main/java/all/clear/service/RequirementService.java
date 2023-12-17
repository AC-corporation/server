package all.clear.service;

import all.clear.domain.Member;
import all.clear.domain.grade.Grade;
import all.clear.domain.requirement.Requirement;
import all.clear.dto.responseDto.RequirementResponseDto;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;

    public void save(Requirement requirement){
        requirementRepository.save(requirement);
    }

    public RequirementResponseDto getRequirement(Long userId){
        return null;
    }
}
