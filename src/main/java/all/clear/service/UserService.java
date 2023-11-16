package all.clear.service;

import all.clear.domain.User;
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
}
