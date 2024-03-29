package allclear.crawl.subject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;

public class ParsingSubject {

    static ArrayList<Subject> subjects = new ArrayList<>();

    // 과목 문자열 파싱함수
    public static ArrayList<Subject> parsingSubjectString(
            ArrayList<String> subjectString) { // 학부전공 과목 파싱 함수
        Subject subject; // 과목 객체

        if (subjectString.isEmpty())
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED);
        for (int i = 0; i < subjectString.size(); ) {
            subject = new Subject(); // subject 객체 생성
            for (int j = 0; j < 11; j++) {
                String str; // 임시 문자열
                if (!subjectString.get(i).isEmpty()) {
                    str = subjectString.get(i); // 문자열 리스트에서 문자열 꺼내기
                } else {
                    str = ""; // null인 경우 ""로 대체
                }
                switch (j) {
                    case 0: // 이수 구분(주전공)
                        if (str.length() > 3) { // 건축학부, 학과 구별위한 조치
                            String subStr = str.substring(3);
                            if (subStr.equals("건축학")
                                    || subStr.equals("건축공학")
                                    || subStr.equals("실내건축")) {
                                str += "전공";
                            }
                        }
                        subject.setMajorClassification(str);
                        break;
                    case 1: // 이수 구분(다전공)
                        subject.setMultiMajorClassification(str);
                        break;
                    case 2: // 공학 인증
                        subject.setEngineeringCertification(str);
                        break;
                    case 3: // 교과 영역
                        subject.setLiberalArtsClassification(str);
                        break;
                    case 4: // 과목 번호
                        subject.setSubjectId(Long.parseLong(str));
                        break;
                    case 5: // 과목명
                        subject.setSubjectName(str);
                        break;
                    case 6: // 분반
                        subject.setClassType(str);
                        break;
                    case 7: // 개설 학과
                        subject.setDepartment(str);
                        break;
                    case 8: // 시간/학점(설계)
                        int slashIndex = str.indexOf("/"); // "/" 문자 위치
                        int bracesIndex = 0; // "(" 문자 위치
                        subject.setSubjectTime(
                                Double.parseDouble(str.substring(0, slashIndex - 1))); // 시간
                        subject.setCredit(
                                Double.parseDouble(
                                        str.substring(slashIndex + 1, slashIndex + 4))); // 학점
                        if (str.length() > 10) { // 설계가 존재할 경우
                            bracesIndex = str.indexOf("(");
                            subject.setDesign(
                                    Integer.parseInt(
                                            str.substring(bracesIndex + 1, bracesIndex + 2))); // 설계
                        }
                        break;
                    case 9: // 강의 시간 및 강의실
                        ArrayList<String> classInfoString =
                                new ArrayList<>(Arrays.asList(str.split("\n")));
                        // 개행문자로 구분된 문자열을 split을 이용해 분할 후 리스트로 변환
                        for (int k = 0; k < classInfoString.size(); k++) {
                            ClassInfo classInfo;
                            classInfo = makeClassInfo(classInfoString.get(k)); // classInfo 생성 후 반환
                            classInfo.setSubject(subject);
                        }
                        break;
                    case 10: // 수강대상
                        subject.setSubjectTarget(str);
                }
                i = i + 1;
            }
            mergeClassInfo(subject.getClassInfoList());
            subjects.add(subject);
        }
        return subjects;
    }

    public static ClassInfo makeClassInfo(String classInfoString) { // ClassInfo 생성 후 반환 해주는 함수
        ClassInfo classInfo;

        String professor = ""; // 교수명
        LocalTime startTime = null; // 강의 시작 시간
        LocalTime endTime = null; // 강의 종료 시간
        String classDay = ""; // 강의 요일
        String classRoom = ""; // 강의 장소

        int strIndex; // 문자열 인덱스
        int startTimeHour;
        int startTimeMinute;
        int endTimeHour;
        int endTimeMinute;

        if (classInfoString.length() < 2) { // 강의시간(강의실)이 존재하지 않을 경우
            classInfo =
                    ClassInfo.builder()
                            .professor("")
                            .classDay("")
                            .startTime(null)
                            .endTime(null)
                            .classRoom("")
                            .build();
            return classInfo;
        }

        strIndex = 0;
        while (true) {
            String str = classInfoString.charAt(strIndex) + "";
            if (str.equals("0") || str.equals("1") || str.equals("2")) break;
            strIndex++; // 강의 시간 문자 인덱스 저장
        }

        for (int i = 0; i < strIndex; ) { // 강의 요일 추출 후 classInfo 생성
            String subStr; // "(" 이후로의 문자열을 저장하기 위한 요소
            ArrayList<String> professorAndClassroom; // 교수명과 강의 장소를 저장하기 위한 리스트

            classDay = classInfoString.charAt(i) + ""; // 강의 요일

            startTimeHour = Integer.parseInt(classInfoString.substring(strIndex, strIndex + 2));
            startTimeMinute =
                    Integer.parseInt(classInfoString.substring(strIndex + 3, strIndex + 5));
            startTime = LocalTime.of(startTimeHour, startTimeMinute, 0); // 강의 시작 시간

            endTimeHour = Integer.parseInt(classInfoString.substring(strIndex + 6, strIndex + 8));
            endTimeMinute =
                    Integer.parseInt(classInfoString.substring(strIndex + 9, strIndex + 11));
            endTime = LocalTime.of(endTimeHour, endTimeMinute, 0); // 강의 종료 시간

            subStr = classInfoString.substring(classInfoString.indexOf("(") + 1);
            professorAndClassroom = new ArrayList<>(Arrays.asList(subStr.split("-")));
            classRoom = professorAndClassroom.get(0); // 강의 장소
            if (professorAndClassroom.size() > 2)
                classRoom = classRoom.concat(professorAndClassroom.get(1));
            professor = professorAndClassroom.get(professorAndClassroom.size() - 1); // 교수명
            professor = professor.substring(0, professor.length() - 1); // 교수명 문자열 중 ")" 제거

            i = i + 2; // 요일의 개수가 여러 개인 경우
        }

        classInfo =
                ClassInfo.builder()
                        .professor(professor)
                        .classDay(classDay)
                        .startTime(startTime)
                        .endTime(endTime)
                        .classRoom(classRoom)
                        .build();
        return classInfo;
    }

    // 20분 이내로 떨어져 있는 강의 시간대 병합
    private static void mergeClassInfo(List<ClassInfo> classInfoList) {
        List<ClassInfo> mergedClassInfoList = new ArrayList<>();

        for (int i = 0; i < classInfoList.size(); ) {
            ClassInfo current = classInfoList.get(i);

            int j = i;
            LocalTime endTime = current.getEndTime();
            for (; j + 1 < classInfoList.size(); j++) {
                ClassInfo next = classInfoList.get(j + 1);
                if (!current.getClassDay().equals(next.getClassDay())
                        || next.getStartTime().isAfter(endTime.plusMinutes(20))
                        || next.getStartTime().isBefore(current.getStartTime())) break;
                endTime = next.getEndTime();
            }
            ClassInfo mergedClassInfo =
                    ClassInfo.builder()
                            .classDay(current.getClassDay())
                            .classRoom(current.getClassRoom())
                            .startTime(current.getStartTime())
                            .endTime(classInfoList.get(j).getEndTime())
                            .build();
            i = j + 1;
            mergedClassInfoList.add(mergedClassInfo);
        }

        // classInfoList를 병합된 결과로 업데이트
        classInfoList.clear();
        classInfoList.addAll(mergedClassInfoList);
    }
}
