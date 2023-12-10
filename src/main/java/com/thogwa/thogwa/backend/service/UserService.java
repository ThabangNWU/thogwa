package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.dto.ResponseDto;
import com.thogwa.thogwa.backend.dto.user.SignInDto;
import com.thogwa.thogwa.backend.dto.user.SignInReponseDto;
import com.thogwa.thogwa.backend.dto.user.SignupDto;
import com.thogwa.thogwa.backend.exception.AuthenticationFailException;
import com.thogwa.thogwa.backend.exception.CustomException;
import com.thogwa.thogwa.backend.model.AuthenticationToken;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {
        // check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // we have an user
            throw new CustomException("user already present");
        }

        if (StringUtils.isBlank(signupDto.getEmail())) {
            throw new CustomException("Email cannot be null or empty");
        }
        // hash the password

        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User();
        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(signupDto.getPassword());

        userRepository.save(user);

        // save the user

        // create the token

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "user created succesfully");
        return responseDto;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInReponseDto signIn(SignInDto signInDto) {
        // Validate input parameters
        if (StringUtils.isBlank(signInDto.getEmail())) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (StringUtils.isBlank(signInDto.getPassword())) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Find user by email
        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("User is not valid");
        }

        // Hash the password
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }

        // Compare the password in DB
        // If password matches, generate authentication token
        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("Token is not present");
        }

        // Return success response with the token
        return new SignInReponseDto("success", token.getToken());
    }

}
