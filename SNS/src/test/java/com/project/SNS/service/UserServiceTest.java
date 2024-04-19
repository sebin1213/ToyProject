package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.fixture.UserEntityFixture;
import com.project.SNS.model.entity.UserEntity;
import com.project.SNS.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserEntityRepository userEntityRepository;



    @Test
    void 회원가입_정상동작() {
        String userName = "name";
        String password = "password";
        UserEntity userEntity= UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        Assertions.assertDoesNotThrow(() -> userService.join(userName,password));
    }


    @Test
    void 회원가입할때_아이디가_중복되면_다르면_에러발생() {
        String userName = "name";
        String password = "password";
        UserEntity userEntity= UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }

    @Test
    void 로그인_정상동작() {
        String userName = "name";
        String password = "password";
        UserEntity userEntity= UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        Assertions.assertDoesNotThrow(() -> userService.login(userName,password));
    }

    @Test
    void 로그인할때_유저가_존재하지_않는경우_에러발생() {
        String userName = "name";
        String password = "password";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }


    @Test
    void 로그인할때_패스워드_다를경우_에러발생() {
        String userName = "name";
        String password = "password";
        String failPassword = "fail";

        UserEntity userEntity= UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class,
                () -> userService.login(userName, failPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}