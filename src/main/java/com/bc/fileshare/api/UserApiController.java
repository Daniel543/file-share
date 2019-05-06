package com.bc.fileshare.api;

import com.bc.fileshare.model.File;
import com.bc.fileshare.model.User;
import com.bc.fileshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("users")
public class UserApiController {

	@Autowired
	UserService userService;



	@PostMapping(value="")
	public void registerUser(@RequestParam("username") String username,
	                          @RequestParam("email") String email, @RequestParam("password") String password) {
		this.userService.addUser(username, email, password);
	}

}
