package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ORDER_ITEM")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long priceAtOrder;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private GoodsEntity goods;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

}
