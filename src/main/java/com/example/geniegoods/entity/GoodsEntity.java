package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_GOODS")
public class GoodsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOODS_ID")
    private Long goodsId;

    @Column(name = "GOODS_URL")
    private String goodsUrl;

    @Column(name = "GOODS_STYLE")
    private String goodsStyle;

    @Column(name = "GOODS_TONE")
    private String goodsTone;

    @Column(name = "GOODS_MOOD")
    private String goodsMood;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "GOODS_IMG_SIZE")
    private Long goodsImgSize;

    @Column(name = "PROMPT")
    private String prompt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_GROUP_ID", insertable = false, updatable = false, nullable = false)
    private UploadImgGroupEntity uploadImgGroupEntity;

    // 연관관계 (필요시)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private GoodsCategoryEntity goodsCategoryEntity;





}