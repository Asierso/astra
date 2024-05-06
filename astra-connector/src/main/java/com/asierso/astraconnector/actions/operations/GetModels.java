package com.asierso.astraconnector.actions.operations;

import com.asierso.astraconnector.AstraConnector;
import com.asierso.astraconnector.actions.Action;
import com.asierso.astracommons.exceptions.RequestException;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;

public class GetModels implements Action {
	@Override
	public Object run(AstraConnector conn) throws RequestException {
		ClientRequest req = new ClientRequest();
		req.setAction(ClientActions.MODEL_LIST);

		ClientResponse res;
		try {
			res = conn.sendRequest(req);
		} catch (Exception e) {
			throw new RequestException(500, "Request process error");
		}

		if (res.getStatus() == 200)
			return res.getOutput();
		else
			throw new RequestException(res.getStatus(), res.getBody());
	}
}
