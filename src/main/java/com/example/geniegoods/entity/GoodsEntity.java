// com.example.geniegoods.entity.GoodsEntity.java

package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "GOODS_IMG_SIZE")
    private Long goodsImgSize;

    @Column(name = "PROMPT")
    private String prompt;

    @Column(name = "USER_ID")
    private Integer userId;  // FK지만 연관관계 안 걸어도 됨 (성능상)

    @Column(name = "UPLOAD_GROUP_ID")
    private Integer uploadGroupId;

    // 연관관계 (필요시)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private GoodsCategoryEntity goodsCategoryEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_GROUP_ID", insertable = false, updatable = false)
    private UploadImgGroupEntity uploadImgGroupEntity;
}