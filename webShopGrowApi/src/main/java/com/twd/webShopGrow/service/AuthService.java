package com.twd.webShopGrow.service;

import com.twd.webShopGrow.dto.ReqRes;
import com.twd.webShopGrow.entity.OurUsers;
import com.twd.webShopGrow.repository.OurUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes signUpRequest, UserDetails currentUser) {
        ReqRes resp = new ReqRes();
        try {

            if (ourUserRepo.findByEmail(signUpRequest.getEmail()).isPresent()) {
                throw new RuntimeException("Email is already taken!");
            }

            if (signUpRequest.getName() == null || signUpRequest.getName().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            // Validate email format
            /*if (!isValidEmail(signUpRequest.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }*/
            if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            // Check if currentUser is Admin1 or Hr_admin
            log.info("yeha tak thik h");
            if (currentUser.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN1"))) {
                return createUser(signUpRequest);
            } else if (currentUser.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("HR_ADMIN"))) {

                if (Objects.equals(signUpRequest.getRole(), "ADMIN1") || Objects.equals(signUpRequest.getRole(), "HR_ADMIN")) {
                    throw new IllegalStateException("Hr_admin cannot add Admin1 or Hr_admin");
                }
                return createUser(signUpRequest);
            } else {
                throw new IllegalStateException("User does not have permission to add users.");
            }
        } catch (Exception e) {

            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }


        return resp;



    }



    private ReqRes createUser(ReqRes signUpRequest) {


        ReqRes resp = new ReqRes();
        try {
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(signUpRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            ourUsers.setRole(signUpRequest.getRole());
            ourUsers.setName(signUpRequest.getName());
            ourUsers.setPhoneNumber(signUpRequest.getPhoneNumber());
            OurUsers ourUserResult = ourUserRepo.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getId() > 0) {
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;

    }

    private boolean isValidEmail(String email) {
        // Implement email format validation logic
        // For example, you can use regex or other validation methods
        // This is a simple example, you might need to enhance it
        return email.matches("[a-zA-Z0-9._%+-]");
    }







































































public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = ourUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }


    public ReqRes signUp1(ReqRes signUpRequest) {
        ReqRes resp = new ReqRes();
        return createUser(signUpRequest);
    }

}
