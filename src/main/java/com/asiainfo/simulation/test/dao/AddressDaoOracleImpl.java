package com.asiainfo.simulation.test.dao;

import javax.sql.DataSource;

import com.asiainfo.simulation.condition.Conditional;
import com.asiainfo.simulation.support.JdbcTemplate;
import com.asiainfo.simulation.test.db.OracleCondition;
import com.asiainfo.simulation.test.model.Address;

@Conditional(OracleCondition.class)
public class AddressDaoOracleImpl implements IAddressDao {

	private JdbcTemplate template;
	public void setDatasource(DataSource datasource) {
		this.template = new JdbcTemplate(datasource);
	}
	
	@Override
	public void save(Address address) {
		
		String sql = "insert into mcd_user_address(user_id, address) values('" + address.getUserid() + "', '" + address.getAddress() + "') ";
		this.template.executeUpdate(sql);
	}
}
