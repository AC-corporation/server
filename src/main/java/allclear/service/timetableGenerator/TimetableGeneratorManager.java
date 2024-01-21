package allclear.service.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorClassInfo;
import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.dto.requestDto.timetable.AddCustomTimetableSubjectRequestDto;
import allclear.dto.requestDto.timetable.ClassInfoRequestDto;
import allclear.dto.requestDto.timetableGenerator.Step1RequestDto;
import allclear.dto.requestDto.timetableGenerator.Step2RequestDto;
import allclear.global.exception.GlobalExceptionHandler;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.timetableGenerator.TimetableGeneratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimetableGeneratorManager {
    @Autowired
    private final TimetableGeneratorRepository timetableGeneratorRepository;

    @Transactional
    public void createTimetableGenerator(Long userId, Step1RequestDto requestDto) {
        TimetableGenerator timetableGenerator = new TimetableGenerator();
        timetableGenerator.setId(userId);
        timetableGenerator.setTableYear(requestDto.getTableYear());
        timetableGenerator.setSemester(requestDto.getSemester());
        timetableGeneratorRepository.save(timetableGenerator);
    }

    @Transactional
    public void addCustomTimetableGeneratorSubjects(Long userId, Step2RequestDto requestDto) {
        TimetableGenerator timetableGenerator = timetableGeneratorRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler(GlobalErrorCode._NO_CONTENTS));

        for (AddCustomTimetableSubjectRequestDto customSubjectRequestDto : requestDto.getCustomTimetableSubjectRequestDtoList()) {

            //ClassInfo 리스트 초기화
            List<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();
            for (ClassInfoRequestDto classInfoRequestDto : customSubjectRequestDto.getClassInfoRequestDtoList()) {
                TimetableGeneratorClassInfo timetableGeneratorClassInfo = TimetableGeneratorClassInfo.createClassInfo(
                        classInfoRequestDto.getProfessor(),
                        classInfoRequestDto.getClassDay(),
                        classInfoRequestDto.getStartTime(),
                        classInfoRequestDto.getEndTime(),
                        classInfoRequestDto.getClassRoom()
                );
                timetableGeneratorClassInfoList.add(timetableGeneratorClassInfo);
            }

            //시간표 생성기 과목 생성해서 시간표 생성기에 추가
            TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject.createCustomTimetableGeneratorSubject(
                    customSubjectRequestDto.getSubjectName(),
                    timetableGeneratorClassInfoList
            );
            timetableGenerator.addTimetableGeneratorSubject(timetableGeneratorSubject);
        }
    }
}
