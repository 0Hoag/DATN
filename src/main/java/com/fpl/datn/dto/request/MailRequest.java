package com.fpl.datn.dto.request;

import lombok.Builder;

@Builder
public record MailRequest(String to, String subject, String text) {
}
