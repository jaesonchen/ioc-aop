package com.asiainfo.simulation.test.dao;

import com.asiainfo.simulation.test.model.User;

public interface IUserDao {
	
	public User query(String id);
	
	public void save(User user);
	
	public void update(User user);
}
