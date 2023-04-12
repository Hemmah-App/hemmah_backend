package org.help.hemah.controller.admin;

import lombok.RequiredArgsConstructor;
import org.help.hemah.domain.user.User;
import org.help.hemah.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // Allow access to anyone temporarily - don't forget to remove this from SecurityConfig as well
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
