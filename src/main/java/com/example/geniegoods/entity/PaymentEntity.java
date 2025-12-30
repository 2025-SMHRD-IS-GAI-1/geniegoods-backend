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
@Table(name = "TB_PAYMENT")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private LocalDateTime paymentAt = LocalDateTime.now();

    @Column(nullable = false)
    private Long amonut;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

}
