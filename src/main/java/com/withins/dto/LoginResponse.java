package com.withins.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {
}
