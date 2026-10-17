package com.himanshu.securepay.dto;
import jakarta.validation.constraints.Pattern;
public record TokenizeRequest(@Pattern(regexp="[0-9]{13,19}", message="PAN must contain 13 to 19 digits") String pan) {}
