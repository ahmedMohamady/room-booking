package com.mashreq.roombooking.service.impl;

import com.mashreq.roombooking.dto.BookingDTO;
import com.mashreq.roombooking.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendBookingEmail(BookingDTO booking) throws MessagingException {
        Context context = new Context();
        context.setVariable("booking", booking);
        String process = templateEngine.process("booking", context);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Conference Reservation");
        helper.setText(process, true);
        helper.setTo(booking.getEmail());
        emailSender.send(mimeMessage);
    }
}
