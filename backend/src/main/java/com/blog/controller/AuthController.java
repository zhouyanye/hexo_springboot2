package com.blog.controller;

import com.blog.model.User;
import com.blog.security.JwtTokenProvider;
import com.blog.service.EmailService;
import com.blog.service.UserService;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final EmailService emailService;
    private final Producer kaptchaProducer;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String storedCode = (String) session.getAttribute("captchaCode");
        String providedCode = loginRequest.get("captcha");
        
        if (!storedCode.equals(providedCode)) {
            return ResponseEntity.badRequest().body("验证码错误");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.get("username"),
                loginRequest.get("password")
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpSession session) {
        String storedCode = (String) session.getAttribute("captchaCode");
        String providedCode = user.getCaptcha();
        
        if (!storedCode.equals(providedCode)) {
            return ResponseEntity.badRequest().body("验证码错误");
        }

        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("邮箱已被注册");
        }

        User savedUser = userService.save(user);
        emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());
        
        return ResponseEntity.ok("注册成功");
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha(HttpSession session) {
        String code = kaptchaProducer.createText();
        session.setAttribute("captchaCode", code);
        
        BufferedImage image = kaptchaProducer.createImage(code);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            return ResponseEntity.ok(Map.of("captcha", "data:image/jpg;base64," + base64Image));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("验证码生成失败");
        }
    }
}