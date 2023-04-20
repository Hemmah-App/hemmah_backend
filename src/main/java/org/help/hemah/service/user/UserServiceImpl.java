package org.help.hemah.service.user;

import lombok.NoArgsConstructor;
import org.help.hemah.config.ResourcesProperties;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.embeded.BaseUserDataEntity;
import org.help.hemah.domain.user.User;
import org.help.hemah.domain.user.UserLanguage;
import org.help.hemah.domain.user.UserStatus;
import org.help.hemah.domain.user.UserType;
import org.help.hemah.domain.volunteer.Volunteer;
import org.help.hemah.exeption.auth.EmailUsedException;
import org.help.hemah.exeption.auth.UsernameUsedException;
import org.help.hemah.exeption.auth.WrongEmailOrPasswordException;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.repository.DisabledRepository;
import org.help.hemah.repository.UserRepository;
import org.help.hemah.repository.VolunteerRepository;
import org.help.hemah.service.auth.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private AuthenticationFacade authenticationFacade;
    private PasswordEncoder passwordEncoder;
    private VolunteerRepository volunteerRepository;
    private DisabledRepository disabledRepository;
    private UserRepository userRepository;

    private ResourcesProperties resourcesProperties;
    private ResourceLoader resourceLoader;
    private Path profilePicsPath;
    private Path DEFAULT_PROFILE_PIC_PATH;

    @Autowired
    public UserServiceImpl(AuthenticationFacade authenticationFacade,
                           PasswordEncoder passwordEncoder,
                           VolunteerRepository volunteerRepository,
                           DisabledRepository disabledRepository,
                           UserRepository userRepository,
                           ResourcesProperties resourcesProperties,
                           ResourceLoader resourceLoader)
            throws IOException {
        this.authenticationFacade = authenticationFacade;
        this.passwordEncoder = passwordEncoder;
        this.volunteerRepository = volunteerRepository;
        this.disabledRepository = disabledRepository;
        this.userRepository = userRepository;

        this.resourcesProperties = resourcesProperties;
        this.resourceLoader = resourceLoader;
        this.profilePicsPath = resourceLoader.getResource(resourcesProperties.profilePicsPath()).getFile().toPath();
        this.DEFAULT_PROFILE_PIC_PATH = profilePicsPath.resolve("default.png");

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User signNewUser(NewUserModel user) {

        if (userRepository.existsByUsername(user.getUserName())) {
            throw new UsernameUsedException("username already used.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailUsedException("Email already used.");
        }

        User newUser = new User();

        BaseUserDataEntity userData = new BaseUserDataEntity(
                user.getUserName(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );

        userData.setAddress(user.getAddress());
        userData.setLongitude(user.getLongitude());
        userData.setLatitude(user.getLatitude());

        newUser.setBaseUserDataEntity(userData);

        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setType(user.getUserType());
        newUser.setLanguage(UserLanguage.AR); // Defaults to Arabic
        newUser.setRoles("USER," + user.getUserType());

        if (user.getUserType() == UserType.VOLUNTEER) {
            Volunteer volunteer = new Volunteer(newUser.getBaseUserDataEntity());
            volunteerRepository.save(volunteer);
        } else if (user.getUserType() == UserType.DISABLED) {
            Disabled disabled = new Disabled(newUser.getBaseUserDataEntity());
            disabledRepository.save(disabled);
        }

        return userRepository.save(newUser);

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
    public void changePassword(String oldPassword, String newPassword) {
        User user = authenticationFacade.getAuthenticatedUser();

        if (!passwordEncoder.matches(oldPassword, user.getBaseUserDataEntity().getPassword())) {
            throw new RuntimeException("Old password is wrong.");
        }

        user.getBaseUserDataEntity().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changeLanguage(UserLanguage language) {
        User user = authenticationFacade.getAuthenticatedUser();

        user.setLanguage(language);
        userRepository.save(user);
    }

    @Override
    public byte[] getProfilePic() throws IOException {
        User user = authenticationFacade.getAuthenticatedUser();

        try {
            return Files.readAllBytes(resourceLoader.getResource(user.getProfilePictureUrl()).getFile().toPath());
        } catch (Exception e) {
            return Files.readAllBytes(this.DEFAULT_PROFILE_PIC_PATH);
        }

    }

    @Override
    public boolean updateProfilePic(MultipartFile profilePic) throws IOException {
        User user = authenticationFacade.getAuthenticatedUser();

        String contentType = profilePic.getContentType();

        if (contentType == null || (!contentType.contains("jpeg") && !contentType.contains("jpg"))) {
            throw new RuntimeException("Only jpeg and jpg files are allowed.");
        }

        String pictureName = user.getBaseUserDataEntity().getUsername() + "." + contentType.split("/")[1];


        FileCopyUtils.copy(profilePic.getBytes(), new File(profilePicsPath.normalize() + "\\" + pictureName));

        user.setProfilePictureUrl(resourcesProperties.profilePicsPath() + "/" + pictureName);
        userRepository.save(user);

        return true;
    }


}
