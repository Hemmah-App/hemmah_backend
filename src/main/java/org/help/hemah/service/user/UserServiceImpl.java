package org.help.hemah.service.user;

import org.help.hemah.exeption.auth.EmailUsedException;
import org.help.hemah.exeption.auth.UsernameUsedException;
import org.help.hemah.exeption.auth.WrongEmailOrPasswordException;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.model.Disabled;
import org.help.hemah.model.User;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.model.enums.UserStatus;
import org.help.hemah.model.Volunteer;
import org.help.hemah.repository.DisabledRepository;
import org.help.hemah.repository.UserRepository;
import org.help.hemah.repository.VolunteerRepository;
import org.help.hemah.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final VolunteerRepository volunteerRepository;
    private final DisabledRepository disabledRepository;
    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(VolunteerRepository volunteerRepository, DisabledRepository disabledRepository, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.volunteerRepository = volunteerRepository;
        this.disabledRepository = disabledRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User signNewUser(NewUserModel user) {

        if (user.getUserType().equals("vol") || user.getUserType().equals("dis")) {

            String username = user.getUserName();
            String email = user.getEmail();

            if (userRepository.existsByUsername(username)) {
                throw new UsernameUsedException("username already used.");
            }

            if (userRepository.existsByEmail(email)) {
                throw new EmailUsedException("Email already used.");
            }

            User newUser = new User();
            newUser.setBaseUserDataEntity(new BaseUserDataEntity(
                    user.getUserName(),
                    passwordEncoder.encode(user.getPassword()),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    "NO ADDRESS FOR NOW",
                    user.getPhoneNumber()
            ));
            newUser.setStatus(UserStatus.ACTIVE);
            newUser.setRoles("ROLE_USER,ROLE_" + user.getUserType().toUpperCase());

            if (user.getUserType().equals("vol")) {
                Volunteer volunteer = new Volunteer(newUser.getBaseUserDataEntity());
                volunteerRepository.save(volunteer);
            } else if (user.getUserType().equals("dis")) {
                Disabled disabled = new Disabled(newUser.getBaseUserDataEntity());
                disabledRepository.save(disabled);
            }

            return userRepository.save(newUser);
        } else {
            throw new IllegalStateException("Wrong UserType.");
        }

    }

    @Override
    public User signinUser(String email, String password) {


        User user = userRepository.findByEmail(email).orElseThrow(() -> new WrongEmailOrPasswordException("Email not found: " + email));


        if (passwordEncoder.matches(
                password, user.getBaseUserDataEntity().getPassword()))
            return user;


        throw new WrongEmailOrPasswordException("Wrong Email or password.");

    }

    @Override
    public List<Volunteer> getActiveVolunteers() {
        return volunteerRepository.findAllByStatus(UserStatus.ACTIVE);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("User not found."));
    }
}
