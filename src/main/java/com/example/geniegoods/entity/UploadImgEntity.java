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
@Table(name = "TB_UPLOAD_IMG")
@EntityListeners(AuditingEntityListener.class)
@ToString
@Builder
public class UploadImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long uploadId;

    @Column(nullable = false)
    private String uploadImgUrl;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Long uploadImgSize;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "upload_group_id")
    private UploadImgGroupEntity uploadImgGroupEntity;


}
