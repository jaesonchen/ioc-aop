package com.asiainfo.simulation.test.service;

import com.asiainfo.simulation.test.model.Address;
import com.asiainfo.simulation.test.model.User;

public interface IUserService {

	public void save(User user);
	
	public User get(String id);
	
	public void saveAll(User user, Address address);
}
