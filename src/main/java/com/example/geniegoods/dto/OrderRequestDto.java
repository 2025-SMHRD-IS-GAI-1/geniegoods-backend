// com.example.geniegoods.dto.OrderRequestDto.java

package com.example.geniegoods.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderRequestDto {

    // 검증 어노테이션 모두 제거 → 그냥 필드만 남김
    private List<OrderItemDto> items = new ArrayList<>();

    private String zipcode;

    private String address;

    private String detailAddress;

    // 내부 클래스도 어노테이션 제거
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrderItemDto {

        private Long goodsId;       // 굿즈 ID

        private Integer quantity;   // 수량
    }
}