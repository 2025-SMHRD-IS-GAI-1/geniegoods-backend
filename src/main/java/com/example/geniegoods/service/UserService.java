package com.example.geniegoods.service;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.UserRepository;
import com.example.geniegoods.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final ObjectStorageService objectStorageService;

	public Map<String, String> getToken(Long userId) {

		// 1. 존재 여부 체크
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

		// JWT 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getNickname());

		Map<String, String> response = new HashMap<>();
		response.put("accessToken", accessToken);

		return response;

	}

	/**
	 * 프로필 이미지 업데이트
	 * 
	 * @param user 현재 로그인한 사용자
	 * @param file 업로드할 이미지 파일
	 * @return 업데이트된 사용자 엔티티
	 */
	@Transactional
	public UserEntity updateProfileImage(UserEntity user, MultipartFile file) throws IOException {
		// 기존 프로필 이미지가 있으면 삭제
		if (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) {
			objectStorageService.deleteProfileImage(user.getProfileUrl());
		}

		// 새로운 프로필 이미지 업로드
		String imageUrl = objectStorageService.uploadProfileImage(file, user.getUserId());

		// 사용자 엔티티 업데이트
		user.setProfileUrl(imageUrl);
		user.setUpdatedAt(LocalDateTime.now());

		return userRepository.save(user);
	}

	/**
	 * 닉네임 중복확인
	 * 
	 * @param nickname 확인할 닉네임
	 * @return 중복 여부 (true : 중복됨, false : 사용가능)
	 */
	public boolean isNicknameExists(String nickname) {
		return userRepository.findByNickname(nickname).isPresent();
	}

	/**
	 * 닉네임 업데이트
	 */
	public void updateNickname(UserEntity user) {
		userRepository.save(user);
	}

	// ===== 회원탈퇴 메서드 (OAuth2 사용자 전용) =====
	@Transactional
	public void withdrawUser(Long userId) {
	    UserEntity user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

	    if (user.isDeleted()) {
	        throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
	    }

	    // socialId는 절대 건드리지 말기! (재가입 시 복구를 위해 그대로 둠)
	    // user.setSocialId("deleted_" + user.getId());  ← 이 줄 삭제 또는 주석 처리!!

	    // 개인정보 마스킹 (복구 시 원복되도록 최소한으로)
	    user.setNickname("탈퇴한 사용자");
	    user.setProfileUrl(null);
	    // socialType은 그대로 두기 (복구 시 필요)

	    user.setDeleted(true);
	    user.setDeletedAt(LocalDateTime.now());

	    userRepository.save(user);
	}

	// ===== 현재 로그인된 사용자 조회 메서드 (필요시 추가, 컨트롤러에서 자주 씀) =====
	public UserEntity findById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	}

	// 또는 nickname으로 찾는 메서드 (Security에서 username이 nickname임)
	public UserEntity findByNickname(String nickname) {
		return userRepository.findByNickname(nickname)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	}
}
