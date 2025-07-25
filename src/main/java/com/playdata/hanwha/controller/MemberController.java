package com.playdata.hanwha.controller;

import com.playdata.hanwha.dto.LoginRequest;
import com.playdata.hanwha.dto.LoginResponse;
import com.playdata.hanwha.entity.Member;
import com.playdata.hanwha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Member member = memberRepository.findByNickname(loginRequest.getNickname())
                .orElse(null);

        if (member == null) {
            return new LoginResponse(false, "존재하지 않는 사용자입니다.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            return new LoginResponse(false, "비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(true, "로그인 성공!");
    }
}
