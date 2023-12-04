package all.clear.service;

import all.clear.domain.User;
import all.clear.dto.requestDto.LoginRequesetDto;
import all.clear.dto.requestDto.UserSignupRequestDto;
import all.clear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//@Transactional
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findOne(Long userId){
        return userRepository.findOne(userId);
    }


    //로그인
    public void login(LoginRequesetDto request){

    }


    //회원가입
    public void createUser(UserSignupRequestDto request){
        User user = User.builder()
                .appId(request.getId())
                .appPassword(request.getPassword())
                .build();
        userRepository.save(user);
    }
}
