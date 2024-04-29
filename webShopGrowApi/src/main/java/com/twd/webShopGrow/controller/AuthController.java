package com.twd.webShopGrow.controller;

import com.twd.webShopGrow.dto.ReqRes;
import com.twd.webShopGrow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@AuthenticationPrincipal UserDetails currentUser, @RequestBody ReqRes signUpRequest){

        ReqRes signUpequest =authService.signUp(signUpRequest, currentUser);
        return ResponseEntity.ok(signUpequest);
    }



    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody ReqRes signInRequest){
        ReqRes signUpequest=authService.signIn(signInRequest);
        return ResponseEntity.ok(signUpequest);
    }
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/signup1")
    public ResponseEntity<?> signUp1( @RequestBody ReqRes signUpRequest){

        ReqRes signUpequest =authService.signUp1(signUpRequest);
        return ResponseEntity.ok(signUpequest);
    }


}
