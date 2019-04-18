package com.asiainfo.simulation.test;

import com.asiainfo.simulation.core.ApplicationContext;
import com.asiainfo.simulation.core.BeanFactory;
import com.asiainfo.simulation.test.model.Address;
import com.asiainfo.simulation.test.model.User;
import com.asiainfo.simulation.test.service.IUserService;

public class TestCase {

	public static void main(String[] args) throws Exception {
		
		BeanFactory applicationContext = new ApplicationContext("beans.xml"); // 获取上下文
        IUserService service = (IUserService) applicationContext.getBean("userService");	//取得bean
        User user = new User();
        user.setUserid("10119");
        user.setUsername("zhangsan");
        service.save(user); // 将user保存入库
        
        User query = service.get("10119");
        System.out.println("user=" + query);
        
        user.setUsername("save address");
       	Address address = new Address(user.getUserid(), "beijing changping");
       	service.saveAll(user, address);
	}
}
