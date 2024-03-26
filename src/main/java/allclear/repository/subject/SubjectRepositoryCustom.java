package allclear.repository.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import allclear.domain.subject.Subject;
import allclear.dto.requestDto.subject.SubjectSearchRequestDto;

public interface SubjectRepositoryCustom {
    Page<Subject> search(SubjectSearchRequestDto request, Pageable pageable);
}
