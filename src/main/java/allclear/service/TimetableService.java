package allclear.service;

import allclear.domain.timetable.Timetable;
import allclear.dto.responseDto.timetable.TimetableResponseDto;
import allclear.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;


    public Timetable findOne(Long id) {
        return timetableRepository.findById(id).get();
    }

    public TimetableResponseDto getTimetable(Long id) {
        return new TimetableResponseDto(findOne(id));
    }

    @Transactional
    public void deleteTimetable(Long id) {
        timetableRepository.deleteById(id);
    }
}
