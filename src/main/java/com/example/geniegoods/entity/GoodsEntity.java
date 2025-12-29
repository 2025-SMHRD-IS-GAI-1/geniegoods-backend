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
@Table(name = "TB_GOODS")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class GoodsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long goodsId;

    @Column(nullable = false)
    private String goodsUrl;

    @Column
    private String goodsStyle;

    @Column
    private String goodsTone;

    @Column
    private String goodsMood;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Long goodsImgSize;

    @Column(nullable = false)
    private String prompt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
