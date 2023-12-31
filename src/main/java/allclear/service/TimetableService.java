package allclear.service;

import allclear.domain.timetable.Timetable;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimetableService {
    @Autowired
    private final TimetableRepository timetableRepository;


    public Timetable findOne(Long id) {
        return timetableRepository.findById(id).get();
    }

    //시간표 조회
    public TimetableResponseDto getTimetable(Long id) {
        return new TimetableResponseDto(findOne(id));
    }

    //시간표 삭제
    @Transactional
    public void deleteTimetable(Long id) {
        timetableRepository.deleteById(id);
    }

    //시간표 업데이트
    //과목 수정
    //과목 조회
    //과목 삭제
    //과목 추가
}
