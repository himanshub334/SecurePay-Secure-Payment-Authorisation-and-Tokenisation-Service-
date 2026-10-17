package com.himanshu.securepay.dto;
import java.time.Instant;
public record TokenizeResponse(String token,String lastFour,Instant createdAt) {}
