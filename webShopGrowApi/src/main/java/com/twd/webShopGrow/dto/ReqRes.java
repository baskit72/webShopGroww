package com.twd.webShopGrow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.twd.webShopGrow.entity.OurUsers;
import com.twd.webShopGrow.entity.enums.UserRoleEnum;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRoleEnum role;
    private String password;
    private OurUsers ourUsers;
}
