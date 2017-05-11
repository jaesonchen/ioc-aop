package com.asiainfo.simulation.tx;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.asiainfo.simulation.aop.advice.IAdvice;
import com.asiainfo.simulation.support.ConnectionHolder;


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
			
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public void afterMethod(Method method) {
		
		try {
			Connection conn = ConnectionHolder.getConnection(datasource);
			ConnectionHolder.release();
			if (ConnectionHolder.ifCommit())
				conn.commit();
			ConnectionHolder.releaseConnection(datasource);
		} catch (SQLException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}
