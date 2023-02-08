package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.helper.res_model.Response;
import org.help.hemah.helper.res_model.UserDataModel;
import org.help.hemah.model.User;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.service.token.TokenService;
import org.help.hemah.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninForm form) {

        Response.ResponseBuilder<?, ?> response = Response.builder()
                .timeStamp(LocalDateTime.now().toString());

        try {
            User user = userService.signinUser(form.getEmail(), form.getPassword());
            String token = tokenService.generateToken(user);
            log.info("Signed in: " + user);
            return ResponseEntity.ok(
                    response
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .message("User signed in successfully")
                            .data(Map.of("token", token))
                            .build());
        } catch (Exception e) {
            log.error("Error Signing User With Creeds: " + form + "reason: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    response
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .message("Error signing user with creeds: " + form)
                            .reason(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody NewUserModel newUserModel) {
        Response.ResponseBuilder<?, ?> response = Response.builder()
                .timeStamp(LocalDateTime.now().toString());

        try {
            User user = userService.signNewUser(newUserModel);
            String token = tokenService.generateToken(user);
            log.debug("new user created: " + user.getBaseUserDataEntity().getUsername());

            return ResponseEntity.ok(
                    response
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .message("User signed up successfully")
                            .data(Map.of("token", token))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    response
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .message("Error Creating User " + newUserModel)
                            .reason(e.getMessage())
                            .build());
        }

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/@me")
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal Jwt jwt) {
        BaseUserDataEntity baseUserData = tokenService.getUser(jwt).getUserData();

        UserDataModel userData = UserDataModel.builder()
                .username(baseUserData.getUsername())
                .email(baseUserData.getEmail())
                .firstName(baseUserData.getFirstName())
                .lastName(baseUserData.getLastName())
                .phoneNumber(baseUserData.getPhoneNumber())
                .address(baseUserData.getAddress())
                .userType(baseUserData.getUserType())
                .build();

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User Data")
                        .data(userData)
                        .build()
        );
    }

}

@Data
class SigninForm {
    private String email;
    private String password;
}

