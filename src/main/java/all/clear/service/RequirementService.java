package all.clear.service;

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

    public Requirement findOne(Long id){
        return requirementRepository.findById(id).get();
    }
    public RequirementResponseDto getRequirement(Long userId){
        return null;
    }
}
