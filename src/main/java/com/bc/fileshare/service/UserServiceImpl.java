package com.bc.fileshare.service;

import com.bc.fileshare.model.User;
import com.bc.fileshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User getUser(int id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	@Override
	public User getUser(String username) {
		Optional<User> user = userRepository.findByUsernameEquals(username);
		return user.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	@Override
	public void addUser(String username, String email, String password) {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}


	private List<SimpleGrantedAuthority> defaultAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUsernameEquals(username);
		if (userOptional.isPresent()){
			User user = userOptional.get();
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), defaultAuthority());
		}
		throw new UsernameNotFoundException("Username or password is invalid");
	}



}
