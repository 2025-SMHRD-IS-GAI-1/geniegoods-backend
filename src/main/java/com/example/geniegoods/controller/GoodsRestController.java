package com.example.geniegoods.controller;

import com.example.geniegoods.dto.goods.GoodsCreateRequestDTO;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsRestController {

    private final GoodsService goodsService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @AuthenticationPrincipal UserEntity user,
            @ModelAttribute GoodsCreateRequestDTO dto  // @RequestBody → @ModelAttribute
    ) {
        log.info("dto :" + dto.toString());
        // MultipartFile[]로 파일 접근 가능
        if (dto.getUploadImages() != null) {
            for (MultipartFile file : dto.getUploadImages()) {
                log.info("File name: {}, Size: {}", file.getOriginalFilename(), file.getSize());
            }
        }
        return ResponseEntity.ok("굿즈 생성 완료");
    }

}
