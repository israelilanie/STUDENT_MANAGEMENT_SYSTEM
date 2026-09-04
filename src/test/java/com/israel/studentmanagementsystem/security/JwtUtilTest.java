package com.israel.studentmanagementsystem.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    @Test
    void generatesExtractsAndValidatesTokens() {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECURE_KEY", "a-very-long-test-secret-key-that-is-at-least-256-bits");
        ReflectionTestUtils.setField(jwtUtil, "DURATION", 60_000L);

        String token = jwtUtil.generateToken("ada@example.com", "STUDENT");

        assertThat(jwtUtil.extractUsername(token)).isEqualTo("ada@example.com");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("STUDENT");
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.validateToken("not-a-token")).isFalse();
    }
}
