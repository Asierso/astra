
package com.asierso.astra.models.requests;

import java.util.List;

/**
 *
 * @author Asierso
 */
public class ClientRequest {
    public enum ClientActions {MODEL,MODEL_LIST,HOOK,HOOK_LIST,EXIT}
    private ClientActions action;
    private List<String> parameters;
    private String model;
    private String hook;

    public ClientActions getAction() {
        return action;
    }

    public void setAction(ClientActions action) {
        this.action = action;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
    
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHook() {
        return hook;
    }

    public void setHook(String hook) {
        this.hook = hook;
    }
    
}
