package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.user.UserStatus;
import org.help.hemah.helper.req_model.ChangeLanguageModel;
import org.help.hemah.helper.req_model.ChangePasswordModel;
import org.help.hemah.helper.res_model.ResponseModel;
import org.help.hemah.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@PreAuthorize("hasRole('USER')")
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserDataController {
    private final UserService userService;

    @PutMapping(value = "/change_password")
    public ResponseEntity<ResponseModel> changePassword(@RequestBody @Valid ChangePasswordModel model) {

        userService.changePassword(model.getOldPassword(), model.getNewPassword());

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Password changed successfully")
                        .build()
        );
    }

    @PutMapping(value = "/change_language")
    public ResponseEntity<ResponseModel> changeLanguage(@RequestBody @Valid ChangeLanguageModel model) {

        userService.changeLanguage(model.getLanguage());

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Language changed successfully")
                        .build()
        );
    }

    @GetMapping(value = "/profile-pic", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePic() {

        return ResponseEntity
                .ok(userService.getProfilePic());
    }

    @PostMapping(value = "/profile-pic")
    public ResponseEntity<ResponseModel> updateProfilePic(@RequestPart MultipartFile profilePic) throws IOException {
        userService.updateProfilePic(profilePic);

        return ResponseEntity
                .ok(ResponseModel.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Profile picture updated successfully")
                        .build()
                );
    }

    @PutMapping(value = "/status/{status}")
    public ResponseEntity<ResponseModel> changeUserStatus(@PathVariable @Valid UserStatus status) {

        userService.changeStatus(status);

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User status changed successfully")
                        .build()
        );
    }

}
