package com.bookshop.profile.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "username is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;
}
