package all.clear.service;

import all.clear.domain.User;
import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import all.clear.dto.responseDto.RequirementComponentResponseDto;
import all.clear.dto.responseDto.RequirementResponseDto;
import all.clear.repository.RequirementComponentRepository;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;
    private final UserService userService;
    private final RequirementComponentRepository requirementComponentRepository;

    public void saveRequirement(Requirement requirement){
        requirementRepository.save(requirement);
    }
    public Requirement findOne(Long id){return requirementRepository.findOne(id);}

    public RequirementResponseDto getRequirement(Long userId){
        User user = userService.findOne(userId);
        Requirement requirement = requirementRepository.findByUserId(userId);
        //userId를 통해 requirementRepo에서 찾은 Requirement를 Dto로 감싸서 반환
        return new RequirementResponseDto(requirement);
    }
}
