package com.exam.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.exam.model.User;
import com.exam.model.UserRole;

@Service
public interface UserService {

	// creating User
	public User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	
	// get user by userName
	public User getUser(String username);
	
	// delete user by id
	
	public void deleteUser(Long userId);
}
