package allclear.crawl.subject;

import java.util.ArrayList;
import java.util.List;

/*
 * 유세인트 서버상에 표기된 이수구분 과목명으로 바꿔주는 클래스
 */
public class ChangeToUsaintSubjectName {

    static List<String> changeList;

    public static List<String> change(String prevString) {

        changeList = new ArrayList<>();
        if (prevString == null)
            return null;

        switch (prevString) {
            case "기독교학과": changeList.add("기독교"); break;
            case "국어국문학과": changeList.add("국문"); break;
            case "영어영문학과": changeList.add("영문"); break;
            case "독어독문학과": changeList.add("독문"); break;
            case "불어불문학과": changeList.add("불문"); break;
            case "중어중문학과": changeList.add("중문"); break;
            case "일어일문학과": changeList.add("일문"); changeList.add("일어일문"); break;
            case "철학과": changeList.add("철학"); break;
            case "사학과": changeList.add("사학"); break;
            case "예술창작학부 문예창작전공": changeList.add("문예창작"); break;
            case "예술창작학부 영화예술전공": changeList.add("영화예술"); break;
            case "스포츠학부": changeList.add("스포츠"); break;
            case "생활체육전공": changeList.add("생활체육전공"); break;
            case "스프츠사이언스전공": changeList.add("스포츠사이언스전공"); break;
            case "수학과": changeList.add("수학"); break;
            case "물리학과": changeList.add("물리"); break;
            case "화학과": changeList.add("화학"); break;
            case "정보통계·보험수리학과": changeList.add("통계·보험"); break;
            case "의생명시스템학부": changeList.add("의생명시스템"); break;
            case "법학과": changeList.add("법학"); break;
            case "국제법무학과": changeList.add("국제법무"); break;
            case "사회복지학부": changeList.add("사회복지"); break;
            case "행정학부": changeList.add("행정학부"); break;
            case "정치외교학과": changeList.add("정외"); break;
            case "정보사회학과": changeList.add("정보사회"); break;
            case "언론홍보학과": changeList.add("언론홍보"); break;
            case "평생교육학과": changeList.add("평생교육"); break;
            case "경제학과": changeList.add("경제"); break;
            case "글로벌통상학과": changeList.add("글로벌통상"); break;
            case "금융경제학과": changeList.add("금융경제"); break;
            case "국제무역학과": changeList.add("국제무역"); break;
            case "통상산업학과(계약학과)": changeList.add("통상산업"); break;
            case "경영학부": changeList.add("경영"); break;
            case "회계학과": changeList.add("회계"); break;
            case "벤처중소기업학과": changeList.add("벤처중소"); break;
            case "금융학부": changeList.add("금융학부"); break;
            case "혁신경영학과(계약학과)": changeList.add("혁신경영"); break;
            case "벤처경영학과(계약학과)": changeList.add("벤처경영"); break;
            case "복지경영학과(계약학과)": changeList.add("복지경영"); break;
            case "회계세무학과(계약학과)": changeList.add("회계세무"); break;
            case "화학공학과": changeList.add("화공"); break;
            case "신소재공학과": changeList.add("신소재"); break;
            case "전기공학부": changeList.add("전기"); break;
            case "기계공학부": changeList.add("기계"); break;
            case "산업·정보시스템공학과": changeList.add("산업·정보"); break;
            case "건축학부": changeList.add("건축학부"); break;
            case "건축공학전공": changeList.add("건축공학"); break;
            case "건축학전공": changeList.add("건축학전공"); break;
            case "실내건축전공": changeList.add("실내건축"); break;
            case "전자정보공학부 전자공학전공": changeList.add("전자공학"); break;
            case "전자정보공학부 IT융합전공": changeList.add("IT융합"); break;
            case "컴퓨터학부": changeList.add("컴퓨터"); break;
            case "소프트웨어학부": changeList.add("소프트"); break;
            case "AI융합학부": changeList.add("AI융합"); break;
            case "글로벌미디어학부": changeList.add("글로벌미디어"); break;
            case "미디어경영학과": changeList.add("미디어경영"); break;
            case "정보보호학과(계약학과)": changeList.add("정보보호"); break;
            case "융합특성화자유전공학부": changeList.add("융합특성화"); break;
            case "차세대반도체학과": changeList.add("차세대반도체"); break;
        }

    return  changeList;
    }
}
