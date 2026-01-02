package com.example.geniegoods.controller;

import com.example.geniegoods.dto.user.NickCheckResponseDTO;
import com.example.geniegoods.dto.user.NickUpdateResponseDTO;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

    private final UserService userService;

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(user);
    }

    /**
     * 닉네임 중복 확인
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<NickCheckResponseDTO> checkNicknameDuplicate(
            @RequestParam("nickname") String nickname,
            @AuthenticationPrincipal UserEntity currentUser) {

        NickCheckResponseDTO response = new NickCheckResponseDTO();

        // 현재 사용자의 닉네임과 같으면 사용 가능
        if (currentUser != null && currentUser.getNickname().equals(nickname)) {
            response.setAvailable(false);
            response.setMessage("현재 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        }

        // 닉네임 중복 확인
        boolean exists = userService.isNicknameExists(nickname);
        response.setAvailable(!exists);
        response.setMessage(exists ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.");

        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 변경
     */
    @GetMapping("/nickname/update")
    public ResponseEntity<NickUpdateResponseDTO> updateNickname(
            @RequestParam("nickname") String newNickname,
            @AuthenticationPrincipal UserEntity currentUser) {

        NickUpdateResponseDTO response = new NickUpdateResponseDTO();

        // validation
        // 프론트쪽에서 무슨 요청을 보낼지 모르니 모든 경우의 수를 막아야 함
        // 현재 사용자의 닉네임과 같으면 변경 안해도됨
        if (currentUser != null && currentUser.getNickname().equals(newNickname)) {
            response.setStatus("SAME_AS_CURRENT");
            response.setMessage("현재 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        }

        // 닉네임 중복 확인
        boolean exists = userService.isNicknameExists(newNickname);

        // 중복확인 했는데 있으면 변경 안해도됨
        if(exists) {
            response.setStatus("DUPLICATED");
            response.setMessage("이미 사용 중인 닉네임입니다.");
            return ResponseEntity.ok(response);
        } else { // 실제로 닉네임 변경
            currentUser.setNickname(newNickname);
            userService.updateNickname(currentUser);
        }

        response.setStatus("UPDATE");
        response.setMessage("닉네임을 " + newNickname + "으로 변경했습니다.");

        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 발급 (포스트맨 테스트용)
     */
    @GetMapping("/token/{userId}")
    public ResponseEntity<Map<String, String>> getToken(@PathVariable("userId") Long userId) {
        Map<String, String> response = userService.getToken(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam("file") MultipartFile file) {
        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 이미지 파일인지 확인
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "이미지 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 파일 크기 제한 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "파일 크기는 10MB를 초과할 수 없습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 프로필 이미지 업데이트
            UserEntity updatedUser = userService.updateProfileImage(user, file);

            Map<String, String> response = new HashMap<>();
            response.put("message", "프로필 이미지가 업로드되었습니다.");
            response.put("profileUrl", updatedUser.getProfileUrl());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
  
  	/**
	 * 회원탈퇴
	 */
	@DeleteMapping("/me/withdraw")
	public ResponseEntity<Map<String, String>> withdraw(
	        @AuthenticationPrincipal UserEntity currentUser) {
	    
	    Map<String, String> response = new HashMap<>();   // ← new HashMap<>() 로 바꾸기!

	    if (currentUser == null) {
	        response.put("message", "로그인된 사용자가 없습니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    if (currentUser.isDeleted()) {
	        response.put("message", "이미 탈퇴 처리된 계정입니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    try {
	        userService.withdrawUser(currentUser.getUserId());
	        response.put("message", "회원탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.error("회원탈퇴 처리 중 오류 발생", e);
	        response.put("message", "탈퇴 처리 중 오류가 발생했습니다.");
	        return ResponseEntity.internalServerError().body(response);
	    }
	}

}
