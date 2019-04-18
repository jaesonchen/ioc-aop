package com.asiainfo.simulation.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.asiainfo.simulation.database.IRowMapper;

public class JdbcTemplate {

	private DataSource datasource;
	public void setDataSource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public JdbcTemplate() {}
	public JdbcTemplate(DataSource datasource) {
		this.setDataSource(datasource);
	}
	
	//这里是实现事物控制的关键
	public Connection getConnection() throws SQLException {
		
		if (ConnectionHolder.isInTransaction()) {
			return ConnectionHolder.getConnection(this.datasource);
		}
		return this.datasource.getConnection();
	}
	
	public void executeUpdate(String sql) {
		
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			convertSQLException(ex);
		}
	}
	
	public <T> List<T> execute(String sql, IRowMapper<T> mapper) {
		
		List<T> result = new ArrayList<T>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			int index = 0;
			while (rs.next()) {
				result.add(mapper.mapRow(rs, index));
			}
		} catch (SQLException ex) {
			convertSQLException(ex);
		} finally {
			try {
    			if (rs != null) {
    				rs.close();
    			}
    			if (stmt != null) {
    				stmt.close();
    			}
			} catch (SQLException ex) {
				convertSQLException(ex);
			}
		}
		return result;
	}
	
	public RuntimeException convertSQLException(SQLException ex) {
		throw new RuntimeException(ex.getMessage());
	}
}
