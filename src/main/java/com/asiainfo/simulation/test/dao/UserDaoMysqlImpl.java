package com.asiainfo.simulation.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.asiainfo.simulation.condition.Conditional;
import com.asiainfo.simulation.database.IRowMapper;
import com.asiainfo.simulation.support.JdbcTemplate;
import com.asiainfo.simulation.test.db.MysqlCondition;
import com.asiainfo.simulation.test.model.User;

@Conditional(MysqlCondition.class)
public class UserDaoMysqlImpl implements IUserDao {

	private JdbcTemplate template;
	public void setDatasource(DataSource datasource) {
		this.template = new JdbcTemplate(datasource);
	}
	
	@Override
	public User query(final String id) {
		
		String sql = "select * from mcd_user where user_id='" + id + "'";
		List<User> list = this.template.execute(sql, new IRowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int index) throws SQLException {
				User user = new User();
				user.setUserid(rs.getString("user_id"));
				user.setUsername(rs.getString("user_name"));
				return user;
			}
		});
		return list == null || list.isEmpty() ? null : list.get(0);
	}
	
	@Override
	public void save(User user) {
		
		String sql = "insert into mcd_user(user_id, user_name) values('" + user.getUserid() + "', '" + user.getUsername() + "') ";
		this.template.executeUpdate(sql);
	}

	/* 
	 * @Description: TODO
	 * @param user
	 * @see com.asiainfo.simulation.test.dao.IUserDao#update(com.asiainfo.simulation.test.model.User)
	 */
	@Override
	public void update(User user) {
		String sql = "update mcd_user set user_name='" + user.getUsername() + "' "
				+ " where user_id='" + user.getUserid() + "'";
		this.template.executeUpdate(sql);
	}
}
