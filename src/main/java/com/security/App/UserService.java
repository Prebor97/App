package com.security.App;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepo userRepo, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Alternatively, inject via constructor
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieves user details by username from the database
        return userRepo.findByEmail(username);
    }

    public ResponseEntity<?> create(UseDto useDto) {
        UserBoy user = new UserBoy();

        user.setFullName(useDto.getLastName()+" "+useDto.getFirstName());
        user.setEmail(useDto.getEmail());
        user.setPassword(passwordEncoder.encode(useDto.getPassword()));
        user.setRoles("ROLE_STUDENT");

        userRepo.save(user);
         String token = jwtUtils.createJwt.apply(loadUserByUsername(user.getUsername()));

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    public List<UserBoy> getUserBoys(){
        return userRepo.findAll();
    }

}
