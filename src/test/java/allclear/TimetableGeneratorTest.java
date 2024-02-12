package allclear;

import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import allclear.service.timetableGenerator.TimetableGeneratorService;
import jakarta.transaction.Transactional;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TimetableGeneratorTest {
    @Autowired
    private TimetableGeneratorRepository timetableGeneratorRepository;
    @Autowired
    private TimetableGeneratorService timetableGeneratorService;

    private void 시간표_생성_테스트() {
        //given

        //when
//        timetableGeneratorService.generateTimetableList();

        //then
    }
}
