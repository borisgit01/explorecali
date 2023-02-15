package com.example.ec.service;

import com.example.ec.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserService service;

    @Test
    public void signup() {
        Optional<User> user = service.signup("dummyUsername", "dummyPassword", "john", "doe");
        assertThat(user.get().getPassword(), not("dummypassword"));
        System.out.println("Encoded Passord = " + user.get().getPassword());
    }
}
