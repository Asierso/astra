package com.asierso.astraconnector.actions;

import com.asierso.astraconnector.AstraConnector;

public interface Action {
	public Object run(AstraConnector connection) throws Exception;
}
