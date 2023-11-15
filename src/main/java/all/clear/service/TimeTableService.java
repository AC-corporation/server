package all.clear.service;

import all.clear.repository.SubjectRepository;
import all.clear.repository.TimeTableRepository;
import all.clear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;


}
