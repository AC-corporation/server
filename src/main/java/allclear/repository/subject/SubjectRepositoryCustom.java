package allclear.repository.subject;

import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.SubjectSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SubjectRepositoryCustom {
    Page<Subject> search(SubjectSearchRequestDto request, Pageable pageable);
}
