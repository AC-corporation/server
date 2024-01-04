package allclear.crawl;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsingSubject {
    /*
    과목 문자열 파싱함수
     */
    static ArrayList<Subject> subjects = new ArrayList<>();

    public static ArrayList<Subject> parsingSubjectString(ArrayList<String> Subjects){ //학부전공 과목 파싱 함수
        Subject subject;
        for(int i=0;i<Subjects.size();){
            subject = new Subject();
            for(int j=0;j<10;j++){
                String str;
                if (!Subjects.get(i).isEmpty()){
                    str = Subjects.get(i); // 문자열 리스트에서 문자열 꺼내기
                }
                else {
                    str = "";
                }
                switch (j){
                    case 0 : // 이수구분(주전공)
                        subject.setMajorClassification(str);break;
                    case 1 : // 이수구분(다전공)
                        subject.setMultiMajorClassification(str);break;
                    case 2 : // 공학인증
                        subject.setEngineeringCertification(str);break;
                    case 3 : // 과목번호
                        subject.setSubjectId(Long.parseLong(str));break;
                    case 4 : // 과목명
                        subject.setSubjectName(str);break;
                    case 5 : // 분반
                        subject.setClassType(str);break;
                    case 6 : // 개설학과
                        subject.setDepartment(str);break;
                    case 7 : // 시간/학점(설계)
                        subject.setSubjectTime(Double.parseDouble(str.substring(0,4))); // 시간
                        subject.setCredit(Integer.parseInt(str.substring(6,7))); // 학점, Double형으로 변환 후 추후 수정 필요
                        if (str.length() > 10) // 설계가 존재할 경우
                            subject.setDesign(Integer.parseInt(str.substring(11, 12))); // 설계
                        break;
                    case 8 : // 강의 시간 및 강의실
                        List<ClassInfo> classInfoList = new ArrayList<>();
                        ArrayList<String> classInfoStringList = new ArrayList<>(Arrays.asList(str.split("\n")));
                        // 개행문자로 구분된 문자열을 split을 이용해 리스트로 변환
                        for(int k=0;k<classInfoStringList.size();k++){
                            ClassInfo classInfo;
                            classInfo = makeClassInfo(classInfoStringList.get(k));
                            classInfoList.add(classInfo);
                        }
                        subject.setClassInfoList(classInfoList);
                        break;
                    case 9 : // 수강대상
                        // 어떻게 파싱할 것인지 추후 논의 필요
                }
                i = i + 1;
            }
            subjects.add(subject);
        }
        return subjects;
    }

    public static ClassInfo makeClassInfo(String classInfoString){ // ClassInfo 생성 후 반환 해주는 함수
        ClassInfo classInfo;
        String professor = ""; // 교수명
        String classTime = ""; //강의 시간
        String classDate = ""; //강의 요일
        String classRoom = ""; //강의 장소
        int strIndex;

        if (classInfoString.length() < 2){ // 강의시간(강의실)이 존재하지 않을 경우
            classInfo = ClassInfo.createClassInfo("","","","");
            return classInfo;
        }

        strIndex = 0;
        while(true){
            String str = classInfoString.charAt(strIndex) + "";
            if (str.equals("0") || str.equals("1") || str.equals("2"))
                break;
            strIndex++; // 강의 시간이 처음으로 시작하는 문자 인덱스 저장
        }

        for(int i=0;i<strIndex;){ // 강의 요일 추출 후 classInfo 생성
            String subStr; // "(" 이후로의 문자열을 저장하기 위한 요소
            ArrayList<String> professorAndClassRoom; // 교수명과 강의 장소를 저장하기 위한 리스트

            classDate = classInfoString.charAt(i) + ""; // 강의 요일
            classTime = classInfoString.substring(strIndex, strIndex + 12); // 강의 시간

            subStr = classInfoString.substring(classInfoString.indexOf("(") + 1);
            professorAndClassRoom = new ArrayList<>(Arrays.asList(subStr.split("-")));

            classRoom = professorAndClassRoom.get(0); // 강의 장소
            professor = professorAndClassRoom.get(1); // 교수명
            professor = professor.substring(0, professor.length()-1); // 교수명 문자열 중 ")" 제거

            i = i + 2; // 요일 추출
        }
        classInfo = ClassInfo.createClassInfo(professor,classTime,classDate,classRoom);
        return classInfo;
    }

}
