package com.avenueCode.springEssentials.controller;

import com.avenueCode.springEssentials.model.User;
import com.avenueCode.springEssentials.repository.UserRepository;
import com.avenueCode.springEssentials.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.ArrayList;
import java.util.Date;
//SecurityMockMvcRequestBuilders
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
class UserControllerUnitTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UserService service;
    @MockBean private UserRepository repository;
    @Autowired private UserController underTest;
    @Autowired protected WebApplicationContext wac;
    @Autowired private ObjectMapper mapper;
    protected User user;
    @BeforeEach
    void setUp(){
        RestAssuredMockMvc.standaloneSetup(this.underTest);
    this.user = new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0)));
    underTest.setId(user.getId());
    repository.save(user);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }
    @Test
    void itShouldGetAllUsers() throws Exception {
        Mockito.when(service.getAllUsers()).thenReturn( new ArrayList<>());
        mockMvc.perform(get("/list-users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldCreateNewUser()throws Exception {
        mockMvc.perform(get("/add-edit-user")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldRedirectHome()  throws Exception{
        mockMvc.perform(get("/home")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldRedirectUserHome() throws Exception{
        Mockito.when(service.getUserById(user.getId())).thenReturn( new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0))));
        mockMvc.perform(get("/homeLog").with(SecurityMockMvcRequestPostProcessors.user("Bruno").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldGetPagLogin() throws Exception {
        mockMvc.perform(get("/login")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldGetProfile()  throws Exception {
        Mockito.when(service.getUserById(user.getId())).thenReturn( new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0))));
        mockMvc.perform(get("/profile").with(SecurityMockMvcRequestPostProcessors.user("Bruno").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void itShouldEditUserById() throws Exception {
        Mockito.when(service.getUserById(user.getId())).thenReturn( new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0))));
        mockMvc.perform(get("/edit-user").with(SecurityMockMvcRequestPostProcessors
                        .user("Bruno").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void itShouldDeleteUserById() throws Exception {
        mockMvc.perform(get("/delete-user").with(SecurityMockMvcRequestPostProcessors.user("Bruno").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }
    @Test
    void itShouldCreateUser() throws Exception {
        Mockito.when(service.createOrUpdateUser(user)).thenReturn(user);
        mockMvc.perform(post("/createUser"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isFound());

    }

    @Test
    void edit() throws Exception {
        mockMvc.perform(post("/edit").with(SecurityMockMvcRequestPostProcessors.user("Bruno").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}