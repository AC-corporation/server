package allclear.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AddCustomTimetableSubjectRequestDto {
    String subjectName;
    String professor;
    List<String> classInfoList; // 수정 필요
}
