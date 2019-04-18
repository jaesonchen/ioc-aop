package com.asiainfo.simulation.support;

import java.sql.Connection;

/**
 * 
 * @Description: 连接代理，包含引用计数
 * @author chenzq  
 * @date 2019年2月4日 上午9:03:26
 * @version V1.0
 */
public class ConnectionHandler {

	private Connection connection;
	private int reference;
	
	public ConnectionHandler(Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public int getReference() {
		return reference;
	}
	public void requested() {
		this.reference++;
	}
	public void release() {
		this.reference--;
	}
}
