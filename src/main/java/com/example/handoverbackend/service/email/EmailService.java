package com.example.handoverbackend.service.email;

import com.example.handoverbackend.domain.member.EmailAuth;
import com.example.handoverbackend.repository.EmailAuthRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmailService {

    private static final int CERTIFICATION_NUMBER_LENGTH = 8;
    private static final int CERTIFICATION_NUMBER_OPTION = 3;
    private static final int NUM_LETTERS_ALPHABET = 26;
    private static final int ASCII_OFFSET_LOWER_A = 97;
    private static final int ASCII_OFFSET_UPPER_A = 65;
    private static final int NUM_DIGITS = 10;

    private String ePw = createKey();
    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender emailSender;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// to => 보내는 대상
        message.setSubject("Hand-Over 회원가입 이메일 인증");// 메일 제목

        // 메일 내용
        // 아래에서 메일의 subtype 을 html 로 지정해주었기 때문인지 html 문법을 사용가능하다
        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요. 숙박, 공연 티켓 양도 플랫폼 Hand-Over 입니다.</h1>";
        msgg += "<h1 style='color:green;'>인증번호 안내 메일입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>" + to + "님 Hand-Over 플랫폼의 회원가입을 환영합니다.<p>";
        msgg += "<br>";
        msgg += "<p>해당 이메일은 회원가입을 위한 인증번호 안내 메일입니다.<p>";
        msgg += "<br>";
        msgg += "<p>하단 인증번호를 '이메일 인증번호' 칸에 입력하여 가입을 완료해주세요.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:green;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        message.setFrom(new InternetAddress("handoverplatform@naver.com", "핸드오버 관리자"));

        return message;
    }

    public String createKey() {

        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < CERTIFICATION_NUMBER_LENGTH; i++) { // 인증코드 8자리
            int index = random.nextInt(CERTIFICATION_NUMBER_OPTION); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (random.nextInt(NUM_LETTERS_ALPHABET)) + ASCII_OFFSET_LOWER_A)); // 영어 소문자
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (random.nextInt(NUM_LETTERS_ALPHABET)) + ASCII_OFFSET_UPPER_A)); // 영어 대문자
                    // A~Z
                    break;
                case 2:
                    key.append((random.nextInt(NUM_DIGITS))); // 숫자
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    @Transactional
    public String sendSimpleMessage(String to) throws Exception {

        MimeMessage message = createMessage(to);

        try {// 예외처리

            if (emailAuthRepository.findEmailAuthByEmail(to) != null) {
                ePw = createKey();
                emailAuthRepository.deleteEmailAuthByEmail(to);
            }
            // 한 사람이 인증코드 여러 번 날릴 시에 인증코드 갱신

            emailSender.send(message); // 메일 발송
        } catch (MailException errorMessage) {
            errorMessage.printStackTrace(); // 에러의 발생근원지를 찾아서 에러 출력
            throw new IllegalArgumentException();
        }

        EmailAuth emailAuth = new EmailAuth(ePw, to);
        emailAuthRepository.save(emailAuth);

        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}

