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
@Table(name = "TB_ORDER")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private LocalDateTime orderedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String detailAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
