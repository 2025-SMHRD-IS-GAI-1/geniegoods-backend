// com.example.geniegoods.entity.OrderEntity.java

package com.example.geniegoods.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ORDER")
public class OrderEntity {

    @Id
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "ORDERED_AT")
    private LocalDateTime orderedAt;

    @Column(name = "TOTAL_AMOUNT")
    private Integer totalAmount;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ZIPCODE")
    private String zipcode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;

    @Column(name = "USER_ID")
    private Long userId;
}