package com.example.safeT.login.config.jwt;

import com.example.safeT.login.entity.User;
import com.example.safeT.login.service.UserService;
import com.example.safeT.login.config.jwt.util.JwtTokenUtil;
import com.example.safeT.login.config.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter : 화면 접속할 때마다 체크

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String register = extractToken(request);

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // AUTHORIZATION이 비어있다면 Jwt token을 전송하지 않음. -> 로그인 하지 않음.
        if(authorizationHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        // AUTHORIZATION 값이 'Bearer'로 시작하지 않을 때, 잘못된 토큰
        if(!authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
        String token = authorizationHeader.split(" ")[1];
        //String token = authorizationHeader.substring(7);

        // Jwt Token 유효성 검사
        if (token == null && !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 유효하면 인증 처리

        // Jwt Token phone 추출
        String phone =  JwtTokenUtil.getPhone(token, secretKey);

        // 추출한 phone으로 User 찾아오기
        User loginUser = userService.getLoginUserByPhone(phone);

        // loginUser 정보로 UsernamePasswordAuthenticationToken 발급
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser.getPhone(), null, List.of(new SimpleGrantedAuthority(loginUser.getGrade().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 권한 부여
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer "를 제외한 JWT 반환
        }
        return null;
    }
}