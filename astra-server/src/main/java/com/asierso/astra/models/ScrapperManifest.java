
package com.asierso.astra.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asierso
 */
public class ScrapperManifest {

    private String name;
    private String version;
    private List<Action> actions;
    private List<Hook> hooks;
    private List<String> args;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Hook> getHooks() {
        return hooks;
    }

    public void setHooks(List<Hook> hooks) {
        this.hooks = hooks;
    }

    public List<String> getHooksNames() {
        ArrayList<String> hooksName = new ArrayList<>();
        for (Hook obj : this.getHooks()) {
            hooksName.add(obj.getName());
        }
        return hooksName;
    }
    
    public List<String> getArgs() {
		return args;
	}
    
    public void setArgs(List<String> args) {
		this.args = args;
	}

}
