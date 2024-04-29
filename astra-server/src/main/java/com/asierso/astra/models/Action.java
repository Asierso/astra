
package com.asierso.astra.models;

import java.util.List;

/**
 *
 * @author Asierso
 */
public class Action implements Cloneable {

	public enum FindableBy {
		ID, CLASS, CSS, TAG, XPATH
	}

	private ActionTypes type;
	private String parameters;
	private List<String> body;
	private String element;
	private int index;
	private FindableBy finder;
	private boolean nooutput;
	private boolean noparams;

	public ActionTypes getType() {
		return type;
	}

	public void setType(ActionTypes type) {
		this.type = type;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public FindableBy getFinder() {
		return finder;
	}

	public void setFinder(FindableBy finder) {
		this.finder = finder;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public boolean isNoOutput() {
		return nooutput;
	}

	public void noOutput(boolean nooutput) {
		this.nooutput = nooutput;
	}

	public boolean isNoParams() {
		return noparams;
	}

	public void setNoParams(boolean noparams) {
		this.noparams = noparams;
	}

	public List<String> getBody() {
		return body;
	}

	public void setBody(List<String> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Action{" + "type=" + type + ", parameters=" + parameters + ", element=" + finder + '}';
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public Action clone() throws CloneNotSupportedException {
		return (Action) super.clone();
	}

}
