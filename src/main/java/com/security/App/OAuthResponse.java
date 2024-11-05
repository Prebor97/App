package com.security.App;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthResponse {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String access_token;
}
