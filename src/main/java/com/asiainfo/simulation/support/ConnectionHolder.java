package com.asiainfo.simulation.support;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionHolder {

	private static final ThreadLocal<ConnectionHandler> local = new ThreadLocal<ConnectionHandler>();
	
	public static boolean isInTransaction() {
		return local.get() != null;
	}
	
	public static void requested() {
		
		ConnectionHandler handle = local.get();
		if (handle == null) {
			return;
		}
		handle.requested();
	}
	
	public static void release() {
		
		ConnectionHandler handle = local.get();
		if (handle == null) {
			return;
		}
		handle.release();
	}
	
	public static boolean ifCommit() {
		
		ConnectionHandler handle = local.get();
		if (handle == null) {
			throw new RuntimeException("connection is null, can not commit transaction!");
		}
		return handle.getReference() == 0;
	}

	public static boolean ifRollback() {
		
		ConnectionHandler handle = local.get();
		if (handle == null) {
			throw new RuntimeException("connection is null, can not rollback transaction!");
		}
		return handle.getReference() == 0;
	}
	
	public static Connection getConnection(DataSource datasource) throws SQLException {
		
		ConnectionHandler handle = local.get();
		if (handle == null) {
			Connection conn = datasource.getConnection();
			handle = new ConnectionHandler(conn);
			local.set(handle);
		}
		
		return handle.getConnection();
	}
	
	public static void releaseConnection(DataSource datasource) throws SQLException {
		
		ConnectionHandler handle = local.get();
		if (handle == null)
			return;
		
		if (handle.getReference() <= 0) {
			local.set(null);
			//datasource..closeConnection(handle.getConnection());
			handle.getConnection().close();
		}
	}
}
