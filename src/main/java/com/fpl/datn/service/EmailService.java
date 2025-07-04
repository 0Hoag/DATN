package com.fpl.datn.service;

import com.fpl.datn.dto.request.ChangePasswordVerifySuccessRequest;
import com.fpl.datn.dto.request.MailRequest;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.ForgotPassword;
import com.fpl.datn.repository.ForgotPasswordRepository;
import com.fpl.datn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender javaMailSender;
    UserRepository userRepository;
    ForgotPasswordRepository forgotPasswordRepository;
    PasswordEncoder passwordEncoder;

    public void sendSimpleMessage(MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.to());
        message.setFrom("zzeross2005@gmail.com");
        message.setSubject(request.subject());
        message.setText(request.text());

        javaMailSender.send(message);
    }

    public String verifyEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INCORRECT));

        forgotPasswordRepository.findByUser(user)
                .ifPresent(fp -> forgotPasswordRepository.deleteById(fp.getFpId()));

        int otp = generateOtp();
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .text("This is OTP for you Forgot Password requesst: " + otp)
                .subject("OTP forgot password request")
                .build();

        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        sendSimpleMessage(mailRequest);
        forgotPasswordRepository.save(forgotPassword);

        return "Email send for verification";
    }

    public String verifyOtp(Integer otp, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INCORRECT));

        var fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpId());
            return "OTP has expired!";
        }

        return "OTP verified!";
    }

    // redirect when verifyOtp success
    public String changePassword(String email, ChangePasswordVerifySuccessRequest request) {
		var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INCORRECT));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_NOT_DUPLICATE_CONFIRM_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));

        userRepository.save(user);

        return "Change password success you can login again!";
    }

    public Integer generateOtp() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
