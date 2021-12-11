package com.avenueCode.springEssentials.service;

import com.avenueCode.springEssentials.model.User;
import com.avenueCode.springEssentials.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Mock private UserRepository repository;
    private AutoCloseable autoCloseable;
    private UserService underTest;
    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserService(repository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllUsers() {
        //when
        underTest.getAllUsers();
        //then
        Mockito.verify(repository).findAll();
    }

    @Test
    void canGetUserById() {
        //given
        User user = new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0)));
        //when
        underTest.getUserById(user.getId());
        //then
        Mockito.verify(repository).findById(user.getId());
    }

    @Test
    void canCreateOrUpdateUser() {
        //given
        User user = new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0)));
        //when
        underTest.createOrUpdateUser(user);
        //then
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(argumentCaptor.capture());
        User userCaptured =argumentCaptor.getValue();
        assertEquals(user,userCaptured);
    }

    @Test
    void canDeleteUserById() {
        //given
        User user = new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0)));
        //when
        underTest.deleteUserById(user.getId());
        // then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(repository).deleteById(argumentCaptor.capture());
        Long captured = argumentCaptor.getValue();
        assertEquals(captured,user.getId());
    }
}