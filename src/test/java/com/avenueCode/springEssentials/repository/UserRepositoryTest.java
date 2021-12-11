package com.avenueCode.springEssentials.repository;

import com.avenueCode.springEssentials.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserRepositoryTest {
    @Autowired private UserRepository underTest;
    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }
    @Test
    void itShouldFindUserByEmail() {
        //given
        User user = new User((long)1,"Bruno","Lima","brunobhsclima@gmail.com","myPassword",new Date(Date.UTC(2002,3,18,0,0,0)));
        underTest.save(user);
        //when
        User exists =underTest.findUserByEmail("brunobhsclima@gmail.com");
        //then
        assertEquals(user,exists);
    }
    @Test
    void itShouldFindUserByEmailDoesNotExists() {
        //given
        String email = "brunobhsclima@gmail.com";
        //when
        User exists =underTest.findUserByEmail(email);
        //then
        assertEquals(exists,null);
    }
}