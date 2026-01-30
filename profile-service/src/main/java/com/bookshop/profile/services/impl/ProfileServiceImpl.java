package com.bookshop.profile.services.impl;

import com.bookshop.profile.dtos.request.ProfileCreateRequest;
import com.bookshop.profile.dtos.request.ProfileUpdateRequest;
import com.bookshop.profile.dtos.response.ProfileResponse;
import com.bookshop.profile.entities.Profile;
import com.bookshop.profile.repositories.ProfileRepository;
import com.bookshop.profile.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public ProfileResponse create(ProfileCreateRequest request) {
        if (profileRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Profile already exists for userId: " + request.getUserId());
        }
        Profile profile = Profile.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName() != null ? request.getFirstName() : "")
                .lastName(request.getLastName() != null ? request.getLastName() : "")
                .build();

        return toResponse(profileRepository.save(profile));
    }

    @Override
    public ProfileResponse getByUserId(String userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));
        return toResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse update(String userId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));
        if (request.getFirstName() != null) profile.setFirstName(request.getFirstName());
        if (request.getLastName() != null) profile.setLastName(request.getLastName());
        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getEmail() != null) profile.setEmail(request.getEmail());
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        profile = profileRepository.save(profile);
        return toResponse(profile);
    }

    private static ProfileResponse toResponse(Profile p) {
        return ProfileResponse.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .username(p.getUsername())
                .email(p.getEmail())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .phone(p.getPhone())
                .avatarUrl(p.getAvatarUrl())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
