package com.springstudy.project.myweddingplanner.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtManager {

    @Value("jwt.secret_key")
    private String secretKey;

    public static String ACCESS_TOKEN_NAME = "accessToken";
    public static String REFRESH_TOKEN_NAME = "refreshToken";

    private final long ONE_MINUTE = 60 * 1000L;
    private final long ONE_DAY = ONE_MINUTE * 60 * 24;
    private final long ACCESS_TOKEN_DURATION = ONE_MINUTE * 30;
    private final long REFRESH_TOKEN_DURATION = ONE_DAY * 30;

    private final UserDetailsService userDetailsService;
    private final RedisTemplate redisTemplate;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateAccessToken(String uniqueValue, Map<String,Object> params) {
        Claims claims = Jwts.claims().setSubject(uniqueValue); // JWT payload 에 저장되는 정보단위
        for(String key : params.keySet()) {
            claims.put(key, params.get(key));
        }

        return getJwt(claims, ACCESS_TOKEN_DURATION);
    }

    public String generateRefreshToken() {
        return getJwt(null, REFRESH_TOKEN_DURATION);
    }

    public String getJwt(Claims claims, long duration) {
        Date currentDate = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(currentDate) // 토큰 발행 시간 정보
                .setExpiration(new Date(currentDate.getTime() + duration)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserIdentifier(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserIdentifier(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public String getTokenByKey(HttpServletRequest request, String key) {
        String getToken = null;

        if(request.getCookies()!=null) {
            for(Cookie c : request.getCookies()) {
                String cookieName = c.getName();
                if(cookieName.equals(key)) {
                    getToken = c.getValue();
                    break;
                }
            }
        }

        return getToken;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean isNotExpired(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public void saveRefreshTokenInStorage(String key, String refreshToken) {
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration validDuration = Duration.ofSeconds(this.REFRESH_TOKEN_DURATION);
        valueOperations.set(key, refreshToken, validDuration);
    }

    public void deleteRefreshTokenInStorage(String key) {
        redisTemplate.delete(key);
    }
}