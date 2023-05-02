package org.help.hemah.service.user;

import org.help.hemah.domain.user.User;
import org.help.hemah.domain.user.UserLanguage;
import org.help.hemah.domain.user.UserStatus;
import org.help.hemah.helper.req_model.NewUserModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User signNewUser(NewUserModel user);

    User signinUser(String email, String password);

    User getUser(String username);

    byte[] getProfilePic();

    boolean updateProfilePic(MultipartFile profilePic) throws IOException;

    void changePassword(String oldPassword, String newPassword);

    void changeLanguage(UserLanguage language);

    void changeStatus(UserStatus status);

}
