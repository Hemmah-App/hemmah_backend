package org.help.hemah.service.user;

import org.help.hemah.domain.user.User;
import org.help.hemah.domain.user.UserLanguage;
import org.help.hemah.helper.req_model.NewUserModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    User signNewUser(NewUserModel user);

    User signinUser(String email, String password);

    User getUser(String username);

    byte[] getProfilePic() throws IOException;

    boolean updateProfilePic(MultipartFile profilePic) throws IOException;

    void changePassword(String oldPassword, String newPassword);

    void changeLanguage(UserLanguage language);

}
