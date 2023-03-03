package com.app.istech.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.istech.Account.LoginUser;
import com.app.istech.Mapper.UserMapper;
import com.app.istech.Model.User;

@Service
public class UserService implements UserDetailsService{
	
	private final UserMapper userMapper;
	
	@Autowired
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = userMapper.findById(userId);
		if(user == null) {
			throw new UsernameNotFoundException("email not found: ");
		}
		return new LoginUser(user);
	}

	
}
