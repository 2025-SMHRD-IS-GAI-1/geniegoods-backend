package com.example.geniegoods.dto.goods;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GoodsCreateRequestDTO {
    private MultipartFile[] uploadImages;
    private String description;
    private String category;
    private String style;
    private String color;
    private String mood;
}
