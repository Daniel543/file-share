package com.bc.fileshare.service;

import com.bc.fileshare.model.User;
import com.bc.fileshare.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@Autowired
	UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getUserById() {
		Optional<User> user = userRepository.findById(1);
		System.out.println(user.get().getFiles());
		assertTrue(user.isPresent());
	}

	@Test
	public void getUserByUsername() {
		Optional<User> user = userRepository.findByUsernameEquals("user");
		assertTrue(user.isPresent());
	}
}