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
@Table(name = "TB_GOODS_VIEW")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class GoodsViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long goodsViewId;

    @Column(nullable = false)
    private LocalDateTime viewedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private GoodsEntity goodsEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private GoodsViewEntity goodsViewEntity;

}
