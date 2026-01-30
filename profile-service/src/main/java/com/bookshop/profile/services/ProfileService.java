package com.bookshop.profile.services;

import com.bookshop.profile.dtos.request.ProfileCreateRequest;
import com.bookshop.profile.dtos.request.ProfileUpdateRequest;
import com.bookshop.profile.dtos.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse create(ProfileCreateRequest request);

    ProfileResponse getByUserId(String userId);

    ProfileResponse update(String userId, ProfileUpdateRequest request);
}
