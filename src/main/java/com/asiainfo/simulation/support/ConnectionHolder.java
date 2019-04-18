package com.asiainfo.simulation.support;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * @Description: 用于持有当前线程的连接，并计算引用计数，在适当的时机进行事物提交
 * @author chenzq  
 * @date 2019年2月4日 上午9:01:29
 * @version V1.0
 */
public class ConnectionHolder {

	private static final ThreadLocal<ConnectionHandler> local = new ThreadLocal<ConnectionHandler>();
	
	public static boolean isInTransaction() {
		return local.get() != null;
	}
	
	public static void requested() {
		
		ConnectionHandler handler = local.get();
		if (handler == null) {
			return;
		}
		handler.requested();
	}
	
	public static void release() {
		
		ConnectionHandler handler = local.get();
		if (handler == null) {
			return;
		}
		handler.release();
	}
	
	public static boolean ifCommit() {
		
		ConnectionHandler handler = local.get();
		if (handler == null) {
			throw new RuntimeException("connection is null, can not commit transaction!");
		}
		return handler.getReference() == 0;
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
			local.set(new ConnectionHandler(datasource.getConnection()));
		}
		return handle.getConnection();
	}
	
	public static void releaseConnection(DataSource datasource) throws SQLException {
		
		ConnectionHandler handler = local.get();
		if (handler == null) {
			return;
		}
		if (handler.getReference() <= 0) {
			local.set(null);
			//datasource.closeConnection(handle.getConnection());
			handler.getConnection().close();
		}
	}
}
