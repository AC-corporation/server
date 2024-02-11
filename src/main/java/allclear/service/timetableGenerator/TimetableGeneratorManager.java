package allclear.service.timetableGenerator;

import allclear.domain.timetableGenerator.TimetableGenerator;
import allclear.domain.timetableGenerator.TimetableGeneratorClassInfo;
import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.domain.timetableGenerator.TimetableGeneratorTimetable;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

class TimetableGeneratorManager {

    //시간표 생성
    static List<TimetableGeneratorTimetable> generateTimetables(TimetableGenerator timetableGenerator) {
        List<TimetableGeneratorSubject> tgSubjectList = timetableGenerator.getTimetableGeneratorSubjectList();
        List<TimetableGeneratorTimetable> newTGTimetableList = new ArrayList<>();
        //필수 수강 과목 스택에 추가
        Stack<TimetableGeneratorSubject> selectedSubjects = new Stack<>();
        selectedSubjects.addAll(tgSubjectList
                .stream()
                .filter(TimetableGeneratorSubject::isSelected)
                .toList()
        );

        //겹치는 시간대, 중복 과목 예외처리(필수 수강 과목)
        for (TimetableGeneratorSubject selectedSubject : selectedSubjects) {
            if (!isTimeSlotAvailable(selectedSubject, selectedSubjects))
                throw new GlobalException(GlobalErrorCode._SCHEDULE_OVERLAPPED);
            if (isOverlappedSubject(selectedSubject, selectedSubjects))
                throw new GlobalException(GlobalErrorCode._SUBJECT_OVERLAPPED);
        }

        //필수 수강 과목 학점 계산
        double creditCount = 0;
        for (TimetableGeneratorSubject selectedSubject : selectedSubjects) {
            if (selectedSubject.getSubject() != null)
                creditCount += selectedSubject.getSubject().getCredit();
        }
        //필수 수강 과목이 아닌 과목
        List<TimetableGeneratorSubject> nonSelectedSubjects = tgSubjectList
                .stream()
                .filter(tgSubject -> !tgSubject.isSelected())
                .filter(tgSubject -> !isOverlappedSubject(tgSubject, selectedSubjects))
                .toList();

        //새 시간표 생성하여 newTGTimetableList에 저장
        generateTimetables(nonSelectedSubjects, newTGTimetableList, selectedSubjects, creditCount);
        if (newTGTimetableList.size() >= 300)
            newTGTimetableList = newTGTimetableList.subList(0, 300);

        return newTGTimetableList;
    }

    //시간표 생성 알고리즘
    private static void generateTimetables(List<TimetableGeneratorSubject> tgSubjectList,
                                    List<TimetableGeneratorTimetable> tgTimetableList,
                                    Stack<TimetableGeneratorSubject> selectedSubjects,
                                    double creditCount) {
        if (creditCount > 18.5 || tgTimetableList.size() > 100000) {
            return;
        } else if (creditCount >= 17.0) {
            tgTimetableList.add(TimetableGeneratorTimetable.createTimetableGeneratorTimetable(selectedSubjects));
        }

        for (TimetableGeneratorSubject checkSubject : tgSubjectList) {
            //이미 선택한 과목 및 유세인트 중복 과목 넘기기
            if (isOverlappedSubject(checkSubject, selectedSubjects)) {
                continue;
            }

            //과목을 추가했을 때 이수 가능한 학점 초과라면 넘기기
            double checkCredit = checkSubject.getSubject() != null ? checkSubject.getSubject().getCredit() : 0;
            if (creditCount + checkCredit > 18.5) {
                continue;
            }

            //충돌 시간대라면 넘기기
            if (!isTimeSlotAvailable(checkSubject, selectedSubjects)) {
                continue;
            }

            selectedSubjects.push(checkSubject);
            generateTimetables(tgSubjectList, tgTimetableList, selectedSubjects, creditCount + checkCredit);
            selectedSubjects.pop();
        }
    }

    //유세인트 중복 과목 체크
    private static boolean isOverlappedSubject(TimetableGeneratorSubject checkSubject,
                                        Collection<TimetableGeneratorSubject> selectedSubjects) {
        if (checkSubject.getSubject() == null)
            return false;
        List<Long> selectedSubjectIdList = selectedSubjects
                .stream()
                .filter(timetableGeneratorSubject -> timetableGeneratorSubject.getSubject() != null)
                .filter(timetableGeneratorSubject -> !timetableGeneratorSubject.equals(checkSubject))
                .map(timetableGeneratorSubject -> timetableGeneratorSubject.getSubject().getSubjectId() / 100)
                .toList();

        //유세인트 중복 과목이면 true
        return selectedSubjectIdList.contains((checkSubject.getSubject().getSubjectId() / 100L));
    }

    //시간대 충돌 체크
    private static boolean isTimeSlotAvailable(TimetableGeneratorSubject checkSubject,
                                        Collection<TimetableGeneratorSubject> selectedSubjects) {
        //이미 선택한 과목들 시간대 추출
        List<TimetableGeneratorClassInfo> selectedClassInfoList = new ArrayList<>();
        for (TimetableGeneratorSubject selectedSubject : selectedSubjects) {
            if (checkSubject.equals(selectedSubject))
                continue;
            selectedClassInfoList.addAll(selectedSubject.getTimetableGeneratorClassInfoList());
        }

        //시간대 충돌 체크
        for (TimetableGeneratorClassInfo checkClassInfo : checkSubject.getTimetableGeneratorClassInfoList()) {
            for (TimetableGeneratorClassInfo selectedClassInfo : selectedClassInfoList) {
                if (checkClassInfo.getClassDay().equals(selectedClassInfo.getClassDay())) { //같은 요일인지 검사
                    LocalTime checkStartTime = checkClassInfo.getStartTime();
                    LocalTime checkEndTime = checkClassInfo.getEndTime();
                    //체크할 수업의 시작시간이 다른 수업시간 전후 10분과 겹치는지 검사
                    if (checkStartTime.isAfter(selectedClassInfo.getStartTime().minusMinutes(10))
                            && checkStartTime.isBefore(selectedClassInfo.getEndTime().plusMinutes(10)))
                        return false;
                    //체크할 수업의 종료시간이 다른 수업시간 전후 10분과 겹치는지 검사
                    if (checkEndTime.isAfter(selectedClassInfo.getStartTime().minusMinutes(10))
                            && checkEndTime.isBefore(selectedClassInfo.getEndTime().plusMinutes(10)))
                        return false;
                }
            }
        }
        return true;
    }


}
