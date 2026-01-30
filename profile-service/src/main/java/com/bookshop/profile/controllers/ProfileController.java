package com.bookshop.profile.controllers;

import com.bookshop.profile.dtos.request.ProfileCreateRequest;
import com.bookshop.profile.dtos.request.ProfileUpdateRequest;
import com.bookshop.profile.dtos.response.ProfileResponse;
import com.bookshop.profile.dtos.response.ResponseSuccess;
import com.bookshop.profile.services.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ResponseSuccess<ProfileResponse>> create(@Valid @RequestBody ProfileCreateRequest request) {
        ProfileResponse data = profileService.create(request);
        return ResponseEntity.status(CREATED).body(new ResponseSuccess<>(CREATED, "Profile created", data));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseSuccess<ProfileResponse>> getByUserId(@PathVariable String userId) {
        ProfileResponse data = profileService.getByUserId(userId);
        return ResponseEntity.ok(new ResponseSuccess<>(OK, "Get profile success", data));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseSuccess<ProfileResponse>> update(
            @PathVariable String userId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        ProfileResponse data = profileService.update(userId, request);
        return ResponseEntity.ok(new ResponseSuccess<>(OK, "Update profile success", data));
    }
}
