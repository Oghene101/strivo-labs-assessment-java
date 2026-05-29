package com.strivolabs.strivolabsassessmentjava.common.security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.util.UuidUtil;
import com.strivolabs.strivolabsassessmentjava.auth.Repositories.RefreshTokenRepository;
import com.strivolabs.strivolabsassessmentjava.auth.Repositories.SessionRepository;
import com.strivolabs.strivolabsassessmentjava.auth.entities.RefreshToken;
import com.strivolabs.strivolabsassessmentjava.auth.entities.Session;
import com.strivolabs.strivolabsassessmentjava.common.dtos.UserDto;
import com.strivolabs.strivolabsassessmentjava.common.security.dtos.HashResponse;
import com.strivolabs.strivolabsassessmentjava.common.security.dtos.TokenResponse;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final long accessTokenExpirationInMs;
    private final int refreshTokenExpirationInDays;
    private final int sessionExpirationInDays;
    private final RefreshTokenRepository refreshTokens;
    private final SessionRepository sessions;
    private final HashingService hashingService;

    public JwtService(
            @Value("${security.jwt.private-key}") String privateKeyStr,
            @Value("${security.jwt.public-key}") String publicKeyStr,
            @Value("${security.jwt.access-token-expiration-ms}") long accessTokenExpirationInMs,
            @Value("${security.jwt.refresh-token-expiration-days}") int refreshTokenExpirationInDays,
            @Value("${security.session-expiration-days}") int sessionExpirationInDays,
            RefreshTokenRepository refreshTokens,
            SessionRepository sessions,
            HashingService hashingService) {

        this.accessTokenExpirationInMs = accessTokenExpirationInMs;
        this.refreshTokenExpirationInDays = refreshTokenExpirationInDays;
        this.sessionExpirationInDays = sessionExpirationInDays;

        this.privateKey = parsePrivateKey(privateKeyStr);
        this.publicKey = parsePublicKey(publicKeyStr);

        this.refreshTokens = refreshTokens;
        this.sessions = sessions;
        this.hashingService = hashingService;
    }

    public TokenResponse generateToken(UserDto user, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationInMs);
        String jti = UUID.randomUUID().toString().replace("-", "");

        Session session = Session.create(
                user.id(),
                jti,
                sessionExpirationInDays,
                user.firstName() + " " + user.lastName());

        String accessToken = generateAccessToken(user, roles, now, expiryDate, jti, session.getId());

        String refreshToken = generateRefreshToken();
        HashResponse refreshTokenHashResponse = hashingService.compute(refreshToken);

        sessions.save(session);

        refreshTokens.save(
                RefreshToken.create(
                        user.id(),
                        session.getId(),
                        refreshTokenHashResponse.hash(),
                        refreshTokenHashResponse.keyId(),
                        OffsetDateTime.now().plusDays(refreshTokenExpirationInDays)));

        return new TokenResponse(accessToken, accessTokenExpirationInMs, refreshToken);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private PrivateKey parsePrivateKey(String key) {
        try {
            String cleanKey = key.replaceAll(
                    "-----\\u0042EGIN RSA PRIVATE KEY-----|-----\\u0045ND RSA PRIVATE KEY-----|-----\\u0042EGIN PRIVATE KEY-----|-----\\u0045ND PRIVATE KEY-----|\\s",
                    "");
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey.getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load RSA private key specification", e);
        }
    }

    private PublicKey parsePublicKey(String key) {
        try {
            String cleanKey = key.replaceAll(
                    "-----\\u0042EGIN RSA PUBLIC KEY-----|-----\\u0045ND RSA PUBLIC KEY-----|-----\\u0042EGIN PUBLIC KEY-----|-----\\u0045ND PUBLIC KEY-----|\\s",
                    "");
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey.getBytes(StandardCharsets.UTF_8));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load RSA public key specification", e);
        }
    }

    private String generateAccessToken(
            UserDto user,
            List<String> roles,
            Date now,
            Date expiryDate,
            String jti,
            UUID sessionId) {
        String accessToken = Jwts.builder()
                .id(jti)
                .claim("sid", sessionId.toString())
                .claim("name", user.firstName() + " " + user.lastName())
                .claim("email", user.email())
                .claim("roles", roles)
                .subject(user.id().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        return accessToken;
    }

    public String generateRefreshToken() {
        byte[] randomBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        String tokenString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        return tokenString;
    }
}