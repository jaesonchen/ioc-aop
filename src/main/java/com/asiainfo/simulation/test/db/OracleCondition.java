package com.asiainfo.simulation.test.db;

import com.asiainfo.simulation.condition.ICondition;
import com.asiainfo.simulation.support.Context;

public class OracleCondition implements ICondition {

	@Override
	public boolean execute() {
		
		return "oracle".equalsIgnoreCase(Context.get("dbtype"));
	}
}
