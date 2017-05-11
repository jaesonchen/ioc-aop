package com.asiainfo.simulation.support;

import java.sql.Connection;

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
