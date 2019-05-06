package com.bc.fileshare.service;

import com.bc.fileshare.model.User;

public interface UserService {

	public User getUser(int id);
	public User getUser(String username);
	public void addUser(String username, String email, String password);
}
