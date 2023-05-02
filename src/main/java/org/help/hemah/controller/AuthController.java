package org.help.hemah.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.embeded.BaseUserDataEntity;
import org.help.hemah.domain.user.User;
import org.help.hemah.domain.user.UserType;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.helper.req_model.SigninModel;
import org.help.hemah.helper.res_model.ResponseModel;
import org.help.hemah.helper.res_model.UserDataModel;
import org.help.hemah.service.auth.AuthenticationFacade;
import org.help.hemah.service.token.TokenService;
import org.help.hemah.service.user.UserService;
import org.help.hemah.service.volunteer.VolunteerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final VolunteerService volunteerService;
    private final TokenService tokenService;
    private final AuthenticationFacade authenticationFacade;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully"),
            @ApiResponse(responseCode = "400", description = "Error signing user with creeds")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninModel form) {

        ResponseModel.ResponseModelBuilder<?, ?> response = ResponseModel.builder()
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
        ResponseModel.ResponseModelBuilder<?, ?> response = ResponseModel.builder()
                .timeStamp(LocalDateTime.now().toString());

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

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/@me")
    public ResponseEntity<?> getUserData() {
        User user = authenticationFacade.getAuthenticatedUser();
        BaseUserDataEntity baseUserData = user.getBaseUserDataEntity();

        UserDataModel.UserDataModelBuilder userData = UserDataModel.builder()
                .username(baseUserData.getUsername())
                .email(baseUserData.getEmail())
                .firstName(baseUserData.getFirstName())
                .lastName(baseUserData.getLastName())
                .phoneNumber(baseUserData.getPhoneNumber())
                .address(baseUserData.getAddress())
                .userType(user.getType())
                .language(user.getLanguage());

        if (user.getType().equals(UserType.VOLUNTEER)) {
            userData.numberOfHelpsProvided(volunteerService.getVolunteerHelpCount(baseUserData.getUsername()));
        }


        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User Data")
                        .data(Map.of("user", userData.build()))
                        .build()
        );
    }

}