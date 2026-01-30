package com.bookshop.identity.services;

import com.bookshop.identity.dtos.request.auth.RegisterRequest;
import com.bookshop.identity.dtos.response.auth.RegisterResponse;

public interface RegisterService {
    RegisterResponse register(RegisterRequest request);
}
