package com.asierso.astraconnector.actions;

import com.asierso.astraconnector.AstraConnector;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;

public class GetModels implements Action{
	@Override
	public Object run(AstraConnector conn) throws Exception {
		ClientRequest req = new ClientRequest();
		req.setAction(ClientActions.MODEL_LIST);
		
		ClientResponse res = conn.sendRequest(req);
		
		if(res.getStatus() == 200)
			return res.getOutput();
		else
			throw new Exception("Error");
	}
}
