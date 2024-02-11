package allclear.domain.timetableGenerator;

import allclear.domain.subject.ClassInfo;
import allclear.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableGeneratorSubject {
    @Id
    @GeneratedValue
    @Column(name = "timetable_generator_subject_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject; //null 인 경우 유저가 정의한 과목
    @Column(name = "subject_name")
    private String subjectName; //과목 이름

    @Setter
    @Column(name = "is_selected")
    private boolean isSelected = false;
    @OneToMany(mappedBy = "timetableGeneratorSubject", cascade = CascadeType.ALL)
    @Column(name = "timetable_generator_class_info_list")
    private List<TimetableGeneratorClassInfo> timetableGeneratorClassInfoList = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_generator_id")
    private TimetableGenerator timetableGenerator;


    //==연관관계 메서드==//
    public void addTimetableGeneratorClassInfo(TimetableGeneratorClassInfo classInfo) {
        this.timetableGeneratorClassInfoList.add(classInfo);
        classInfo.setTimetableGeneratorSubject(this);
    }


    //==생성 메서드==//

    /**
     * 실제 과목
     */
    public static TimetableGeneratorSubject createActualTimetableGeneratorSubject(Subject subject) {
        TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject
                .builder()
                .subject(subject)
                .subjectName(subject.getSubjectName())
                .timetableGeneratorClassInfoList(new ArrayList<>())
                .build();
        for (ClassInfo classInfo : subject.getClassInfoList()) {
            timetableGeneratorSubject.addTimetableGeneratorClassInfo(
                    TimetableGeneratorClassInfo
                            .builder()
                            .professor(classInfo.getProfessor())
                            .classDay(classInfo.getClassDay())
                            .startTime(classInfo.getStartTime())
                            .endTime(classInfo.getEndTime())
                            .classRoom(classInfo.getClassRoom())
                            .build()
            );
        }
        return timetableGeneratorSubject;
    }

    /**
     * 유저가 정의한 과목
     */
    public static TimetableGeneratorSubject createCustomTimetableGeneratorSubject(String subjectName,
                                                                                  List<TimetableGeneratorClassInfo> classInfoList) {
        TimetableGeneratorSubject timetableGeneratorSubject = TimetableGeneratorSubject
                .builder()
                .subject(null)
                .subjectName(subjectName)
                .timetableGeneratorClassInfoList(new ArrayList<>())
                .build();
        for (TimetableGeneratorClassInfo classInfo : classInfoList) {
            timetableGeneratorSubject.addTimetableGeneratorClassInfo(classInfo);
        }
        return timetableGeneratorSubject;
    }
}
