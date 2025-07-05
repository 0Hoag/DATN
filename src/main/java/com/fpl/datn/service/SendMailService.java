package com.fpl.datn.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.repository.OrderRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMailService {
    JavaMailSender mailSender;
    OrderRepository orderRepository;
    ExportPDFService exportPDFService;
    SpringTemplateEngine templateEngine;

    private static final String SUBJECT = "Thông tin hàng của ";
    private static final String SHIPPED = "Thông tin trạng thái đơn hàng của ";

    private void sendMail(String to, String subject, String body, byte[] attachmentData, String fileName)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true nếu gửi HTML

        // Đính kèm file từ byte[]
        if (attachmentData != null && attachmentData.length > 0) {
            ByteArrayResource attachment = new ByteArrayResource(attachmentData);
            helper.addAttachment(fileName, attachment);
        }

        mailSender.send(message);
    }

    public void sendInvoiceToUser(int orderId) throws Exception {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String to = order.getUser().getEmail();
        String subject = SUBJECT + order.getAddress().getFullName();
        String body = templateEngine.process("thank-you", new Context());
        byte[] pdf = exportPDFService.exportPdf(orderId);
        String filename = "hoa-don-" + orderId + ".pdf";
        sendMail(to, subject, body, pdf, filename);
    }

    public void sendInvoiceToUserUpdateStatus(int orderId) throws Exception {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String to = order.getUser().getEmail();
        String shipped = SHIPPED + order.getAddress().getFullName();
        Context context = new Context();
        context.setVariable("orderId", orderId);
        String body = templateEngine.process("shipping", context);
        sendMail(to, shipped, body, null, null);
    }
}
