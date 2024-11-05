package com.security.App;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.function.Function;

@Component
public class GoogleJwtUtils {

    private UserRepo userRepo;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils utils;

    @Autowired
    public GoogleJwtUtils(UserRepo userRepo, @Lazy UserService userService, PasswordEncoder passwordEncoder, JwtUtils utils) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;;
    }



    private final Function<GoogleOAuthDto, OAuthResponse> getUserFromIdToken = (oAuthDto) -> {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .build();

        GoogleIdToken token = null;
        try {
            token = verifier.verify(oAuthDto.getIdToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (token != null) {
            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            return OAuthResponse
                    .builder()
                    .email(email)
                    .first_name(givenName)
                    .last_name(familyName)
                    .password("GOOGLELOGIN1")
                    .build();
        }
        return null;
    };

    public Function<OAuthResponse, OAuthUserResponse> saveOauthUser = userDto -> {
        if (userRepo.findByEmail(userDto.getEmail())!=null){
            UserDetails userDetails = userService.loadUserByUsername(userDto.getEmail());
            return getAuthUserResponse(userDto ,(UserBoy)userDetails);
        }
        else{
            UserBoy userBoy = new UserBoy();
            userBoy.setFullName(userDto.getFirst_name() + " " + userDto.getLast_name());
            userBoy.setEmail(userDto.getEmail());
            userBoy.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userBoy.setRoles("ROLE_USER");
            UserBoy savedUser = userRepo.save(userBoy);
            return getAuthUserResponse(userDto, savedUser);
        }
    };

    public OAuthUserResponse getAuthUserResponse(OAuthResponse authDto, UserBoy userBoy){
        UserDetails userDetails = userService.loadUserByUsername(userBoy.getUsername());
        String token = utils.createJwt.apply(userDetails);

        OAuthUserResponse oAuthUserResponse = new OAuthUserResponse();
        oAuthUserResponse.setId(userBoy.getId());
        oAuthUserResponse.setEmail(userBoy.getUsername());
        oAuthUserResponse.setFirst_name(authDto.getFirst_name());
        oAuthUserResponse.setLast_name(authDto.getLast_name());
        oAuthUserResponse.setFullname(authDto.getFirst_name() + " " + authDto.getLast_name());
        oAuthUserResponse.setRole(userBoy.getRoles());
        oAuthUserResponse.setAccess_token(token);
        return oAuthUserResponse;
    }

    OAuthLastUserResponse oAuthLastUserResponse(UserOAuthDetails userOAuthDetails){
        OAuthLastUserResponse oAuthLastUserResponse = new OAuthLastUserResponse();
        oAuthLastUserResponse.setUser(userOAuthDetails);
        return oAuthLastUserResponse;
    }

    UserOAuthDetails userOAuthDetailsResponse(OAuthUserResponse oAuthUserResponse){
        UserOAuthDetails userOAuthDetails = new UserOAuthDetails();
        userOAuthDetails.setId(oAuthUserResponse.getId());
        userOAuthDetails.setEmail(oAuthUserResponse.getEmail());
        userOAuthDetails.setFirst_name(oAuthUserResponse.getFirst_name());
        userOAuthDetails.setLast_name(oAuthUserResponse.getLast_name());
        userOAuthDetails.setFullname(oAuthUserResponse.getFullname());
        userOAuthDetails.setRole(oAuthUserResponse.getRole());
        return userOAuthDetails;
    }

    public OAuthBaseResponse googleOauthUserJWT(GoogleOAuthDto googleOAuthDto){
        OAuthResponse user =  getUserFromIdToken.apply(googleOAuthDto);
        OAuthUserResponse data = saveOauthUser.apply(user);
        UserOAuthDetails userOAuthDetails = userOAuthDetailsResponse(data);
        OAuthLastUserResponse lastData = oAuthLastUserResponse(userOAuthDetails);
        return new OAuthBaseResponse(HttpStatus.OK.value(), "User Created Successful!", data.getAccess_token(), lastData);
    }
}
