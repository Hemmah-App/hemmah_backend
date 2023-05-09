package org.help.hemah.service.user;

import lombok.SneakyThrows;
import org.help.hemah.domain.user.UserType;
import org.help.hemah.helper.req_model.NewUserModel;
import org.help.hemah.service.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWJkdWxsYWgiLCJleHAiOjE2OTExOTY0MjQsImlhdCI6MTY4MjU1NjQyNCwicm9sZXMiOiJVU0VSLERJU0FCTEVEIn0.Poh3EmkM-3w7EZq_VzouisyE52x5LOXI7JyxbhaYG58htFP_NqrkRz_J8aJY1oP49VXCj452C_1EPTccjNgDUXoA7upRegxXje9nz80nEqMsFTXyK9A9SANHgGApdE7gy6-z4MTxoX6fwx0N1VKEz-be2I2HJGFqd1Jqeve0GeudsLXkDjdBN0Zz-Ss2-PXQWy3RCZcHVKJulpCXtWpAWwyqnbVrKEu0pazCaPB86TZpO-oF7GqOzGD30V_7UQCxFmhpP8jPpShPY03tXYr51xDdh6w1-BY6V3CUE-Z_gRfT0nk8sdoraSDjEWHEVoPkxF95-iAytIYEkfEiszI24w";

    @Autowired
    TokenService tokenService;
    @Autowired
    UserService underTest;

    @BeforeEach
    void addUserToContext() {
        SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(token));
    }

    @Test
    void createNewUser() {
        // given
        NewUserModel user = NewUserModel.builder()
                .userName("roqia_hani")
                .email("roqia@gmail.com")
                .password("Password@1")
                .userType(UserType.VOLUNTEER)
                .firstName("roqia")
                .lastName("abdelhameed")
                .address("address")
                .phoneNumber("1234567890")
                .latitude(0.0)
                .longitude(0.0)
                .build();

        // when
        underTest.signNewUser(user);

        // then
        assertThat(underTest.getUser("roqia_hani")).isNotNull();
    }


    @Test
    @SneakyThrows
    void checkIfUserProfilePhotoChanged() {

        // given
        byte[] old = underTest.getProfilePic();

        // when
        Random random = new Random();
        MultipartFile profilePic = new MockMultipartFile(
                "photo",
                "profilePic",
                "image/png",
                new byte[]{(byte) random.nextInt(), (byte) random.nextInt()}); // Image bytes instead

        underTest.updateProfilePic(profilePic);

        // then
        assertThat(underTest.getProfilePic()).isNotEqualTo(old);
    }


}
