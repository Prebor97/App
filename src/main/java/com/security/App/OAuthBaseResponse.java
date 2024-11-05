package com.security.App;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthBaseResponse {
    private int status_code;
    private String message;
    private String access_token;
    private OAuthLastUserResponse data;
}
