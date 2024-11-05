package com.security.App;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class SecurityController {
    @Autowired
    UserService userService;

    @Autowired
    GoogleJwtUtils googleJwtUtils;

    @GetMapping("/home")
    public String home() {
        return "This is Home";
    }

    @GetMapping("/student")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User.getAttributes();
    }

    @GetMapping("/admin")
    public String admin() {
        return "This is Admin";
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UseDto useDto) {
        if (useDto.getPassword() == null || useDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        return userService.create(useDto);
    }

    @GetMapping("/getStudents")
    public ResponseEntity<?> getAllUsers() {
        List<UserBoy> userBoys = userService.getUserBoys();
        return new ResponseEntity<>(userBoys, HttpStatus.OK);
    }

    @GetMapping("/google")
    public ResponseEntity<?> handleGoogleAuth(@RequestBody GoogleOAuthDto payload) {
        return ResponseEntity.ok(googleJwtUtils.googleOauthUserJWT(payload));

    }
}