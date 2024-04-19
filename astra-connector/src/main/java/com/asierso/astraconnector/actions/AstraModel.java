package com.asierso.astraconnector.actions;

import java.util.List;

import com.asierso.astracommons.exceptions.HookException;
import com.asierso.astracommons.exceptions.RequestException;
import com.asierso.astracommons.requests.ClientActions;
import com.asierso.astracommons.requests.ClientRequest;
import com.asierso.astracommons.requests.ClientResponse;
import com.asierso.astraconnector.AstraConnector;

public class AstraModel {
	private final String name;
	private final List<String> hooks;
	private final AstraConnector conn;

	public AstraModel(String name, List<String> hooks, AstraConnector conn) {
		this.name = name;
		this.hooks = hooks;
		this.conn = conn;
	}

	public List<String> getHooks() {
		return hooks;
	}

	public String getName() {
		return name;
	}

	public Object runHook(String hook,List<String> params) throws HookException, RequestException, Exception {
		if(!hooks.contains(hook)) {
			throw new HookException("Hook not found");
		}
		
		return conn.fetch(new Action() {
			@Override
			public Object run(AstraConnector connection) throws RequestException, Exception {
				ClientRequest req = new ClientRequest();
				req.setAction(ClientActions.HOOK);
				req.setModel(name);
				req.setHook(hook);
				req.setParameters(params);

				ClientResponse res;

				// Execute hook
				try {
					res = conn.sendRequest(req);
				} catch (Exception e) {
					throw new RequestException(500, "Request process error");
				}

				if (res.getStatus() == 200) {
					return res;

				} else
					throw new RequestException(res.getStatus(), res.getBody());
			}
		});
	}
}
