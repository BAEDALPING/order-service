package com.baedalping.delivery.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportReviewRequestDto {

    @NotBlank(message = "신고 메시지는 필수 입력 사항입니다.")
    private String reportMessage;
}