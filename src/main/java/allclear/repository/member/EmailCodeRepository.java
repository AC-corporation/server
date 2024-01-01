package allclear.repository.member;

import allclear.domain.member.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    //emailCode(email, code)와 id 저장

    public Optional<EmailCode> findByEmailAndCode(String email, String code);
}
