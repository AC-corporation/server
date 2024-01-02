package allclear.crawl;

import allclear.domain.member.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CrawlSubjectTest {
    @Test
    public static void main(String[] args) {
        ArrayList<String> majorSubjectList = new ArrayList<>();
        CrawlSubjectInfo crawlSubjectInfo = new CrawlSubjectInfo("ID", "PWD");
        majorSubjectList = crawlSubjectInfo.getMajorSubjects();
        for(int i=0;i<majorSubjectList.size();i++){
            String majorSubject = majorSubjectList.get(i);
            System.out.println(majorSubject);
        }

    }
}
