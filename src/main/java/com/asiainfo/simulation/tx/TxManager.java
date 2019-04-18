package com.asiainfo.simulation.tx;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.asiainfo.simulation.aop.advice.IAdvice;
import com.asiainfo.simulation.support.ConnectionHolder;

/**
 * 
 * @Description: 事务管理器，aop实现，在进入方法时获取连接，退出时释放连接，并判断是否提交事务
 * @author chenzq  
 * @date 2019年2月4日 上午9:05:22
 * @version V1.0
 */
public class TxManager implements IAdvice {

	private DataSource datasource;
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	@Override
	public void beforeMethod(Method method) {

		try {
			Connection conn = ConnectionHolder.getConnection(datasource);
			ConnectionHolder.requested();
			
			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public void afterMethod(Method method) {
		
		try {
			Connection conn = ConnectionHolder.getConnection(datasource);
			ConnectionHolder.release();
			if (ConnectionHolder.ifCommit()) {
				conn.commit();
			}
			ConnectionHolder.releaseConnection(datasource);
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/* 
	 * @Description: TODO
	 * @param method
	 * @see com.asiainfo.simulation.aop.advice.IAdvice#onException(java.lang.reflect.Method)
	 */
	@Override
	public void onException(Method method) {
		
		try {
			Connection conn = ConnectionHolder.getConnection(datasource);
			ConnectionHolder.release();
			if (ConnectionHolder.ifRollback()) {
				conn.rollback();
			}
			ConnectionHolder.releaseConnection(datasource);
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}
