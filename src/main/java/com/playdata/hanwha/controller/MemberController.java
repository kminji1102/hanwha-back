package com.playdata.hanwha.controller;

import com.playdata.hanwha.dto.LoginRequest;
import com.playdata.hanwha.entity.Member;
import com.playdata.hanwha.repository.MemberRepository;
import com.playdata.hanwha.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Member> findAll() { return memberRepository.findAll(); }

    @PostMapping(value = "/join", consumes = {"multipart/form-data"})
    public Member joinMember(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("nickName") String nickName,
            @RequestPart("password") String password
    ) throws IOException {
        String imagePath = null;

        if (image != null && !image.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            imagePath = uploadDir + image.getOriginalFilename();
            image.transferTo(new File(imagePath)); // 서버에 파일 저장
        }

        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .imagePath(imagePath)
                .nickname(nickName)
                .password(encodedPassword)
                .build();

        return memberRepository.save(member);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        Member member = memberRepository.findByNickname(loginRequest.getNickname())
                .orElse(null);

        if (member == null) {
            response.put("success", false);
            response.put("message", "존재하지 않는 사용자입니다.");
            return response;
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 발급
        String token = JwtUtil.generateToken(member.getNickname());
        response.put("success", true);
        response.put("token", token);
        response.put("message", "로그인 성공!");

        return response;
    }
}
