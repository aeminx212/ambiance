package com.aMinx.ambiance.controllerTests;

import com.aMinx.ambiance.controllers.AuthenticationController;
import com.aMinx.ambiance.data.UserDAO;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.dto.LoginFormDTO;
import com.aMinx.ambiance.models.dto.RegisterFormDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class AuthenticationControllerTest{

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    ExtendedModelMap model;

    @Mock
    HttpSession session;

    @Mock
    User user;

    @Mock
    RegisterFormDTO registerFormDTO;

    @Mock
    LoginFormDTO loginFormDTO;

    @Mock
    Errors errors;

    @Mock
    HttpServletRequest request;

    @Mock
    UserDAO userDAO;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateRegistration(){
        assertThat("register", is(authenticationController.createRegistration(model)));
    }

    @Test
    public void testProcessRegistration_HasErrors(){
        when(errors.hasErrors()).thenReturn(true);
        assertThat("register", is(authenticationController.processRegistration(registerFormDTO, errors, request, model)));
    }


    @Test
    public void testProcessRegistration_PasswordDoesNotMatchVerify(){
        RegisterFormDTO registerFormDTOPasswordError = new RegisterFormDTO();
        registerFormDTOPasswordError.setPassword("NNNNNN");
        registerFormDTOPasswordError.setVerifyPassword("LLLLLLL");
        assertThat("register", is(authenticationController.processRegistration(registerFormDTOPasswordError, errors, request, model)));
    }

    @Test
    public void testProcessRegistration_InfoUserNotNull(){
        when(userDAO.findByUsername(registerFormDTO.getUsername())).thenReturn(user);
        assertThat("register", is(authenticationController.processRegistration(registerFormDTO, errors, request, model)));
    }

    @Test
    public void testProcessRegistration_PasswordsDoNotMatch(){
        when(userDAO.findByUsername(registerFormDTO.getUsername())).thenReturn(null);
        String password = "pasSWord";
        String verifyPassword = "password";
        when(registerFormDTO.getPassword()).thenReturn(password);
        when(registerFormDTO.getVerifyPassword()).thenReturn(verifyPassword);
        assertThat("register", is(authenticationController.processRegistration(registerFormDTO, errors, request, model)));
    }

    @Test
    public void testProcessRegistration(){
        when(userDAO.findByUsername(registerFormDTO.getUsername())).thenReturn(null);
        String password = "password";
        when(registerFormDTO.getPassword()).thenReturn(password);
        when(registerFormDTO.getVerifyPassword()).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        assertThat("redirect:/user/dashboard", is(authenticationController.processRegistration(registerFormDTO, errors, request, model)));
    }

    @Test
    public void testCreateLogin() {
        assertThat("login", is(authenticationController.createLogin(model)));
    }

    @Test
    public void testProcessLogin_HasErrors(){
        LoginFormDTO loginFormDTONoUserName = new LoginFormDTO();
        loginFormDTONoUserName.setUsername("");
        assertThat("login", is(authenticationController.processLogin(loginFormDTONoUserName, errors, request, model)));
    }

    @Test
    public void testProcessLogin_UserEqualsNull(){
        LoginFormDTO loginFormDTONullUser = new LoginFormDTO();
        when(userDAO.findByUsername(loginFormDTO.getUsername())).thenReturn(null);
        assertThat("login", is(authenticationController.processLogin(loginFormDTONullUser, errors, request, model)));
    }

    @Test
    public void testProcessLogin_WrongPassword(){
        LoginFormDTO loginFormDTOErrorPassword = new LoginFormDTO();
        loginFormDTOErrorPassword.setPassword("");
        String password = "password";
        when(userDAO.findByUsername(loginFormDTO.getUsername())).thenReturn(user);
        when(user.isMatchingPassword(password)).thenReturn(false);
        assertThat("login", is(authenticationController.processLogin(loginFormDTOErrorPassword, errors, request, model)));
    }

    @Test
    public void testProcessLogin(){
        String password = loginFormDTO.getPassword();
        when(userDAO.findByUsername(loginFormDTO.getUsername())).thenReturn(user);
        when(user.isMatchingPassword(password)).thenReturn(true);
        when(request.getSession()).thenReturn(session);
        assertThat("redirect:/user/dashboard", is(authenticationController.processLogin(loginFormDTO, errors, request, model)));
    }

    @Test
    public void testLogout(){
        when(request.getSession()).thenReturn(session);
        assertThat("redirect:/login", is(authenticationController.logout(request)));
    }
}
