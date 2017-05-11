package com.asiainfo.simulation.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowMapper<T> {

	public T mapRow(ResultSet rs, int index)  throws SQLException;
}
