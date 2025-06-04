package com.springnext.demo.auth.controller;

import com.springnext.demo.auth.controller.request.AuthRequest;
import com.springnext.demo.auth.controller.response.AuthResponse;
import com.springnext.demo.security.CustomUserDetailsService;
import com.springnext.demo.security.JwtUtil;
import com.springnext.demo.security.entity.RefreshToken;
import com.springnext.demo.security.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApiV1AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        String accessToken = jwtUtil.generateToken(authRequest.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(authRequest.getUsername());

        // RefreshToken DB 저장 또는 갱신
        refreshTokenRepository.save(new RefreshToken(
                authRequest.getUsername(), refreshToken, LocalDateTime.now().plusDays(7)
        ));

        // accessToken은 쿠키로
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true).secure(true).path("/").maxAge(15 * 60).build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // refreshToken도 쿠키로
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(7 * 24 * 60 * 60).build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // return ResponseEntity.ok("로그인 성공");

        // 응답 본문에도 포함
        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", accessToken);
        // body.put("refreshToken", refreshToken);
        // body.put("expiresIn", 60 * 15);

        return ResponseEntity.ok(body);

        /*
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(jwt));
        */
    }

    // ✅ 토큰 유효성 및 payload 확인용 엔드포인트
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        String username = authentication.getName(); // JWT로부터 추출된 사용자 이름

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("message", "Token is valid");

        return ResponseEntity.ok(payload);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token invalid");
        }

        String username = jwtUtil.extractUsername(refreshToken);

        RefreshToken saved = refreshTokenRepository.findById(username).orElse(null);
        if (saved == null || !saved.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token mismatch");
        }

        String newAccessToken = jwtUtil.generateToken(username);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true).secure(true).path("/").maxAge(15 * 60).build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // return ResponseEntity.ok("Access token 재발급 완료");

        // 응답 본문에도 포함
        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", newAccessToken);
        // body.put("refreshToken", refreshToken);
        // body.put("expiresIn", 60 * 15);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String username = jwtUtil.extractUsernameFromRequest(request);

        refreshTokenRepository.deleteById(username); // DB에서 삭제

        // 쿠키 삭제
        ResponseCookie deleteAccess = ResponseCookie.from("accessToken", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).build();
        ResponseCookie deleteRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }
}
