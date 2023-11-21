package all.clear.controller;

import all.clear.service.RequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requirement")
public class RequirementController {
    private final RequirementService requirementService;


    //@Operation(summary = "졸업요건 조회", description = "졸업요건 조회")
    //@GetMapping("/look") // string 수정 필요
    public ResponseEntity get(Long userId){
        return new ResponseEntity(requirementService.getRequirement(userId), HttpStatus.OK);
    }
}
