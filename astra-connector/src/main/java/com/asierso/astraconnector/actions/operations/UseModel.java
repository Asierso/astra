package com.asierso.astraconnector.actions.operations;

import com.asierso.astraconnector.AstraConnector;
import com.asierso.astraconnector.actions.Action;
import com.asierso.astraconnector.actions.AstraModel;
import com.asierso.astracommons.exceptions.RequestException;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;

public class UseModel implements Action {
	private String modelName;

	public UseModel(String model) {
		this.modelName = model;
	}

	@Override
	public Object run(AstraConnector conn) throws RequestException, Exception {
		ClientRequest req = new ClientRequest();
		req.setAction(ClientActions.MODEL);
		req.setModel(modelName);

		ClientResponse res;

		//Get model name and load in astra server
		try {
			res = conn.sendRequest(req);
		} catch (Exception e) {
			throw new RequestException(500, "Request process error");
		}

		if (res.getStatus() == 200) { //If request is done, list hooks
			req.setAction(ClientActions.HOOK_LIST);
			try {
				//List hooks and return model with hooks avaiable
				res = conn.sendRequest(req);
				if (res.getStatus() == 200)
					return new AstraModel(req.getModel(), res.getOutput(),conn);
				else
					throw new RequestException(res.getStatus(), res.getBody());
			} catch (Exception e) {
				throw new RequestException(500, "Request process error");
			}
		} else
			throw new RequestException(res.getStatus(), res.getBody());
	}
}
