// com.example.geniegoods.entity.OrderItemEntity.java

package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ORDER_ITEM")
public class OrderItemEntity {

    @Id
    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PRICE_AT_ORDER")
    private Integer priceAtOrder;  // 주문 당시 가격 스냅샷!!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID")
    private GoodsEntity goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;
}