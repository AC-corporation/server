package allclear.dto.requestDto.subject;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateSubjectRequestDto {
    @NotBlank
    private Integer year;
    @NotBlank
    private String semester;
    @NotBlank
    private String usaintId;
    @NotBlank
    private String usaintPassword;
}
