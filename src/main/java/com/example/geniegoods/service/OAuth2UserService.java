package com.example.geniegoods.service;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * OAuth2 사용자 정보 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class OAuth2UserService {

	private final UserRepository userRepository;

	/**
	 * OAuth2 사용자 정보로부터 사용자 엔티티 생성 또는 조회
	 * @param oauth2User OAuth2 사용자 정보
	 * @param registrationId 소셜 로그인 제공자 (google, kakao, naver)
	 * @return 사용자 엔티티
	 */
	@Transactional
	public UserEntity processOAuth2User(OAuth2User oauth2User, String registrationId) {
		// 소셜 타입 정규화 (google -> GOOGLE)
		String socialType = registrationId.toUpperCase();
		
		// 소셜 ID 추출
		String socialId = extractSocialId(oauth2User, registrationId);
		
		// 기존 사용자 조회
		Optional<UserEntity> existingUser = userRepository.findBySocialTypeAndSocialId(socialType, socialId);
		
		if (existingUser.isPresent()) {
			// 기존 사용자 정보 업데이트
			UserEntity user = existingUser.get();
			updateUserInfo(user, oauth2User, registrationId);
			return userRepository.save(user);
		} else {
			// 신규 사용자 생성
			return createNewUser(oauth2User, socialType, socialId, registrationId);
		}
	}

	/**
	 * 소셜 ID 추출 (제공자별로 다름)
	 */
	private String extractSocialId(OAuth2User oauth2User, String registrationId) {
		Map<String, Object> attributes = oauth2User.getAttributes();
		
		return switch (registrationId.toLowerCase()) {
			case "google" -> (String) attributes.get("sub");
			case "kakao" -> String.valueOf(attributes.get("id"));
			case "naver" -> {
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				yield (String) response.get("id");
			}
			default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
		};
	}

	/**
	 * 사용자 정보 추출 (제공자별로 다름)
	 */
	private UserInfo extractUserInfo(OAuth2User oauth2User, String registrationId) {
		Map<String, Object> attributes = oauth2User.getAttributes();
		
		return switch (registrationId.toLowerCase()) {
			case "google" -> new UserInfo(
					(String) attributes.get("name"),
					(String) attributes.get("picture")
			);
			case "kakao" -> {
				Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
				Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
				yield new UserInfo(
						(String) profile.get("nickname"),
						(String) profile.get("profile_image_url")
				);
			}
			case "naver" -> {
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				yield new UserInfo(
						(String) response.get("name"),
						(String) response.get("profile_image")
				);
			}
			default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
		};
	}

	/**
	 * 기존 사용자 정보 업데이트
	 */
	private void updateUserInfo(UserEntity user, OAuth2User oauth2User, String registrationId) {
		UserInfo userInfo = extractUserInfo(oauth2User, registrationId);
		user.setNickname(userInfo.nickname());
		user.setProfileUrl(userInfo.profileUrl() != null ? userInfo.profileUrl() : "");
		user.setUpdatedAt(LocalDateTime.now());
	}

	/**
	 * 신규 사용자 생성
	 */
	private UserEntity createNewUser(OAuth2User oauth2User, String socialType, String socialId, String registrationId) {
		UserInfo userInfo = extractUserInfo(oauth2User, registrationId);
		
		UserEntity newUser = UserEntity.builder()
				.nickname(userInfo.nickname())
				.socialType(socialType)
				.socialId(socialId)
				.profileUrl(userInfo.profileUrl() != null ? userInfo.profileUrl() : "")
				.role("USER")
				.createdAt(LocalDateTime.now())
				.build();
		
		return userRepository.save(newUser);
	}

	/**
	 * 사용자 정보 DTO
	 */
	private record UserInfo(String nickname, String profileUrl) {}
}

