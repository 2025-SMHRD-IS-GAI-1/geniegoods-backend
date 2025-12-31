package com.example.geniegoods.service;

import com.example.geniegoods.dto.OrderRequestDto;
import com.example.geniegoods.dto.OrderRequestDto;
import com.example.geniegoods.entity.*;
import com.example.geniegoods.repository.GoodsRepository;
import com.example.geniegoods.repository.OrderItemRepository;
import com.example.geniegoods.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GoodsRepository goodsRepository;
    private final UserService userService;

    private static final int SHIPPING_FEE = 3000;

    @Transactional
    public OrderEntity createOrder(UserEntity user, OrderRequestDto dto) {

        // ====================== 1. 입력 검증 (필수!) ======================
        if (dto == null) {
            throw new IllegalArgumentException("주문 요청 데이터가 없습니다.");
        }

        List<OrderRequestDto.OrderItemDto> items = dto.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문할 굿즈를 하나 이상 선택해야 합니다.");
        }

        if (dto.getZipcode() == null || dto.getZipcode().trim().isEmpty()) {
            throw new IllegalArgumentException("우편번호는 필수입니다.");
        }

        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        }
        // =================================================================

        // 주문 기본 정보 생성
        OrderEntity order = OrderEntity.builder()
                .user(user)
                .orderedAt(LocalDateTime.now())
                .status("PENDING")
                .zipcode(dto.getZipcode().trim())
                .address(dto.getAddress().trim())
                .totalAmount(0)
                .detailAddress(dto.getDetailAddress() != null ? dto.getDetailAddress().trim() : null)
                .build();

        // 반드시 먼저 저장
        orderRepository.save(order);

        int subtotal = 0;

        // 주문 상품 처리 + 개별 검증
        for (OrderRequestDto.OrderItemDto itemDto : items) {

            // 개별 아이템 검증
            if (itemDto.getGoodsId() == null) {
                throw new IllegalArgumentException("굿즈 ID는 필수입니다.");
            }
            if (itemDto.getQuantity() == null || itemDto.getQuantity() < 1) {
                throw new IllegalArgumentException("각 굿즈의 수량은 1개 이상이어야 합니다.");
            }

            // 상품 존재 여부 확인
            GoodsEntity goods = goodsRepository.findById(itemDto.getGoodsId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 굿즈입니다: " + itemDto.getGoodsId()));

            // 카테고리 가격 가져오기 (null 체크 추가로 안전하게)
            GoodsCategoryEntity category = goods.getGoodsCategoryEntity();
            if (category == null || category.getPrice() == null) {
                throw new IllegalStateException("굿즈에 연결된 카테고리 또는 가격 정보가 없습니다: " + itemDto.getGoodsId());
            }
            Integer categoryPrice = category.getPrice();

            // 주문 아이템 생성
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .goods(goods)
                    .quantity(itemDto.getQuantity())
                    .priceAtOrder(categoryPrice)  // 주문 당시 가격 스냅샷
                    .order(order)
                    .build();

            orderItemRepository.save(orderItem);
            
            // 소계 누적
            subtotal += categoryPrice * itemDto.getQuantity();
        }

        // 총 금액 계산 (상품 합계 + 배송비)
        order.setTotalAmount(subtotal + SHIPPING_FEE);

        return order;
    }
}