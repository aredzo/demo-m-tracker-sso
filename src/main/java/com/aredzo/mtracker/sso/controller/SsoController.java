package com.aredzo.mtracker.sso.controller;


import com.aredzo.mtracker.sso.dto.ErrorResponse;
import com.aredzo.mtracker.sso.dto.TokenResponse;
import com.aredzo.mtracker.sso.dto.UserPostRequest;
import com.aredzo.mtracker.sso.dto.UserResponse;
import com.aredzo.mtracker.sso.dto.UserTokenResponse;
import com.aredzo.mtracker.sso.dto.ValidateTokenResponse;
import com.aredzo.mtracker.sso.service.SsoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@Api(value = "Sso user controller")
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "User Not Found", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "User Not Authorized", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
})
public class SsoController {

    private final SsoService ssoService;

    public SsoController(SsoService ssoService) {
        this.ssoService = ssoService;
    }

    @PostMapping("/users/signup")
    @ApiOperation("Signup new user")
    public UserTokenResponse addNewUser(@Valid @RequestBody UserPostRequest requestBody) {
        return ssoService.addNewUser(requestBody.getEmail(), requestBody.getPassword());
    }

    @PostMapping("/users/login")
    @ApiOperation("Login user and receive token")
    public TokenResponse loginUser(@Valid @RequestBody UserPostRequest requestBody) {
        return ssoService.login(requestBody.getEmail(), requestBody.getPassword());
    }

    @GetMapping("/users/{userId}")
    @ApiOperation("Get user by id")
    public UserResponse getUserByUserId(@NotNull @NotEmpty @PathVariable int userId) {
        return ssoService.getUserWithId(userId);
    }

    @GetMapping("/users/")
    @ApiOperation("Get all registered user")
    public List<UserResponse> getAllUsers() {
        return ssoService.getAllUsers();
    }

    @GetMapping("/token/{token}")
    @ApiOperation("Validate user token and get userId")
    public ValidateTokenResponse validateUserToken(@NotNull @PathVariable UUID token) {
        return ssoService.validateToken(token);
    }
}
