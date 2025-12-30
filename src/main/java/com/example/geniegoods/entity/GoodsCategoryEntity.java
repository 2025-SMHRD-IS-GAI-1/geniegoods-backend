package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_GOODS_CATEGORY")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class GoodsCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String koreanName;

    @Column(nullable = false)
    private int price;
}
