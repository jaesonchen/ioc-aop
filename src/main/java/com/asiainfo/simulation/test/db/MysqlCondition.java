package com.asiainfo.simulation.test.db;

import com.asiainfo.simulation.condition.ICondition;
import com.asiainfo.simulation.support.Context;

public class MysqlCondition implements ICondition {

	@Override
	public boolean execute() {
		
		return "mysql".equalsIgnoreCase(Context.get("dbtype"));
	}
}
