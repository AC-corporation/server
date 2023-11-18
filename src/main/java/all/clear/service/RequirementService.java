package all.clear.service;

import all.clear.domain.User;
import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import all.clear.repository.RequirementComponentRepository;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private RequirementRepository requirementRepository;
    private UserService userService;
    private RequirementComponentRepository requirementComponentRepository;


    public Requirement findOne(Long id){return requirementRepository.findOne(id);}

    public List<RequirementComponent> getRequirementComponent(Long userId){
//        User user = userService.findOne(userId);
        List<RequirementComponent> requirementComponentList = requirementRepository.getRequirementComponent(userId);
        return requirementComponentList;
    }
    //Dto로 감싸서 내보내야 함 -> 반환타입 수정 필요
}
