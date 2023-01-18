package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.model.User;
import org.help.hemah.service.TokenService;
import org.help.hemah.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninFrom form) {
        try {
            User user = userService.signinUser(form.getEmail(), form.getPassword());
            String token = tokenService.generateToken(user);
            log.debug("Signed in: " + user);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("Message", e.getMessage());
            log.error("Error Signing User With Creds: " + form + "\nreason: " + e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody NewUserModel newUserModel) {
        try {
            User user = userService.signNewUser(newUserModel);
            log.debug("new user created: " + user.getBaseUserDataEntity().getUsername());
            Map<String, Object> body = new HashMap<>();
            body.put("token", tokenService.generateToken(user.getBaseUserDataEntity().getUsername()));
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            Map<String, Object> body = new HashMap<>();
            body.put("Message", e.getMessage());
            log.error("Error Creating User " + newUserModel + "\nreason: " + e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }

    }
}

@Data
class SigninFrom {
    private String email;
    private String password;
}

