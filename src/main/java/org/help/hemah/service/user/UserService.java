package org.help.hemah.service.user;

import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.model.User;
import org.help.hemah.model.Volunteer;

import java.util.List;

public interface UserService {

    User signNewUser(NewUserModel user);

    User signinUser(String email, String password);

    List<Volunteer> getActiveVolunteers();

    User getUserByUsername(String username);

    String getUserRoles(String username);

}
