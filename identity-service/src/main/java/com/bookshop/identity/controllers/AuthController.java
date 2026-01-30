package com.bookshop.identity.controllers;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import com.bookshop.identity.dtos.response.auth.RegisterResponse;
import com.bookshop.identity.dtos.base.ResponseSuccess;
import com.bookshop.identity.services.impl.RegisterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterServiceImpl registerServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<ResponseSuccess<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse data = registerServiceImpl.register(request);
        return ResponseEntity.status(CREATED).body(new ResponseSuccess<>(CREATED, "Register success", data));
    }
}
