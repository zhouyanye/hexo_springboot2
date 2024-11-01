package com.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("欢迎注册我们的博客系统");
        message.setText("亲爱的 " + username + "，\n\n" +
                "感谢您注册我们的博客系统！您的账号已经成功创建。\n\n" +
                "祝您使用愉快！\n\n" +
                "博客团队");
        mailSender.send(message);
    }
}