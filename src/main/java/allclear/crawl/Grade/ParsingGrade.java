package allclear.crawl.Grade;

import java.util.ArrayList;
import java.util.List;

import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.grade.SemesterSubject;

public class ParsingGrade {
    /*
    성적 문자열 파싱함수
    */
    static Grade grade;
    static List<Long> prevSubjectIdList = new ArrayList<>(); // 수강한 과목 Id 리스트

    public static Grade parsingGradeString(
            String totalCredit,
            String averageGrade,
            ArrayList<String> entireGrades,
            ArrayList<String> detailGrades) {

        String year; // 학년도
        String semester; // 학기
        String semesterTitle = ""; // 학년도 + 학기, 학기명
        String semesterAverageGrade = ""; // 학기 평균 학점

        grade =
                Grade.builder()
                        .totalCredit(Double.parseDouble(totalCredit))
                        .averageGrade(averageGrade)
                        .build();
        ArrayList<SemesterSubject> semesterSubjectList = new ArrayList<>(); // 학기별 과목 리스트

        for (int i = 0; i < detailGrades.size(); i++) {

            if (i + 5 >= detailGrades.size()) break;
            if (detailGrades.get(i).contains("*")) { // 학년도 학기 문자열이면 *를 포함하고 있음
                if (!semesterSubjectList.isEmpty()) {
                    SemesterGrade semesterGrade =
                            SemesterGrade.createSemesterGrade(
                                    grade,
                                    semesterAverageGrade,
                                    semesterTitle,
                                    semesterSubjectList);
                    grade.addSemesterGrade(semesterGrade);
                }
                semesterSubjectList = new ArrayList<>();
                year = detailGrades.get(i).substring(1, 5); // 해당 성적의 학년도 추출 ex) 2024학년도 2024추출
                semester =
                        detailGrades
                                .get(i)
                                .substring(9, 11)
                                .strip(); // 해당 성적의 학기 추출 ex) 겨울학기 중 겨울 추출
                semesterTitle = detailGrades.get(i).substring(1).strip(); // ex) 2024학년도1학기
                semesterAverageGrade =
                        getSemesterAverageGrade(
                                year, semester, entireGrades); // 년도와 학기를 통해 해당 학기 평균 성적 추출
            } else {
                // 과목명과 등급을 통해 SemesterSubject 생성
                semesterSubjectList.add(
                        SemesterSubject.createSemesterSubject(
                                detailGrades.get(i + 2), detailGrades.get(i + 1)));
                prevSubjectIdList.add(Long.parseLong(detailGrades.get(i + 5))); // 과목 ID 추출
                i = i + 5; // 다음 과목으로 이동
            }
        }
        if (!semesterSubjectList.isEmpty()) { // 마지막 학기 추가
            SemesterGrade semesterGrade =
                    SemesterGrade.createSemesterGrade(
                            grade, semesterAverageGrade, semesterTitle, semesterSubjectList);
            grade.addSemesterGrade(semesterGrade);
        }

        return grade;
    }

    /*
    평균 성적 추출 함수
     */
    public static String getSemesterAverageGrade(
            String year, String semester, ArrayList<String> entireGrades) {
        String tmpYear = "";
        String tmpSemester = "";
        String semesterAverageGrade = "";

        for (int i = 0; i < entireGrades.size() - 1; i++) {
            if (i + 5 >= entireGrades.size()) break;
            if (entireGrades.get(i).isEmpty()) // 값이 비어있다면 값을 가져오지 않음
            continue;
            if (entireGrades.get(i + 1).isEmpty()) // 값이 비어있다면 값을 가져오지 않음
            continue;
            if (entireGrades.get(i + 5).isEmpty()) // 값이 비어있다면 값을 가져오지 않음
            continue;
            tmpYear = entireGrades.get(i);
            tmpSemester = entireGrades.get(i + 1).substring(0, 2).strip();
            if (tmpYear.equals(year) && tmpSemester.equals((semester))) {
                semesterAverageGrade = entireGrades.get(i + 5);
                break;
            }
        }
        return semesterAverageGrade;
    }
}
