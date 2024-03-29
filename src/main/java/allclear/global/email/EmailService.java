package allclear.global.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // 보낼 메일 주소
        message.setFrom("allclear0829.company@gmail.com");
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
