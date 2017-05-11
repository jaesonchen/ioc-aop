package com.asiainfo.simulation.test.service;

import com.asiainfo.simulation.test.dao.IAddressDao;
import com.asiainfo.simulation.test.dao.IUserDao;
import com.asiainfo.simulation.test.model.Address;
import com.asiainfo.simulation.test.model.User;

public class UserServiceImpl implements IUserService {

	private IUserDao userDao;
	private IAddressDao addressDao;
	  
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setAddressDao(IAddressDao addressDao) {
		this.addressDao = addressDao;
	}
	
	@Override
	public void save(User user) {
		this.userDao.save(user);
	}

	@Override
	public User get(String id) {
		return this.userDao.query(id);
	}
	
	@Override
	public void saveAll(User user, Address address) {
		
		this.userDao.update(user);
		this.addressDao.save(address);
		//throw new RuntimeException("ex");
	}
}
