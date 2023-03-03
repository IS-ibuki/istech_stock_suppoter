package com.app.istech.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.app.istech.Model.User;

@Mapper
public interface UserMapper {
	
	public User findById(String userId);
	
}
