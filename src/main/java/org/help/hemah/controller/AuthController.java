package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.helper.res_model.UserDataModel;
import org.help.hemah.model.User;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.service.token.TokenService;
import org.help.hemah.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
        try {
            User user = userService.signinUser(form.getEmail(), form.getPassword());
            log.info("Signed in: " + user);
            return ResponseEntity.ok(Map.of("token", tokenService.generateToken(user)));
        } catch (Exception e) {
            log.error("Error Signing User With Creeds: " + form + "reason: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("Message", e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody NewUserModel newUserModel) {
        try {
            User user = userService.signNewUser(newUserModel);
            log.debug("new user created: " + user.getBaseUserDataEntity().getUsername());

            return ResponseEntity.ok(Map.of("token", tokenService.generateToken(user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Error Creating User " + newUserModel + "\nreason: " + e.getMessage()));
        }

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/@me")
    public UserDataModel getUserData(@AuthenticationPrincipal Jwt jwt) {
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

        return userData;
    }

}

@Data
class SigninForm {
    private String email;
    private String password;
}

