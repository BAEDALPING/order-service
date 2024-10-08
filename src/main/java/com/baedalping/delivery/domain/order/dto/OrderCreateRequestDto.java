package com.baedalping.delivery.domain.order.dto;

import com.baedalping.delivery.domain.order.entity.OrderType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OrderCreateRequestDto {

    @NotNull(message = "Address ID cannot be null")
    @Pattern(
        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
        message = "Address ID must be a valid UUID"
    )
    private String addressId;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

}
