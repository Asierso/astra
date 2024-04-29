
package com.asierso.astra.scrapper;

import com.asierso.astracommons.exceptions.HookException;
import com.asierso.astra.extensions.RegexExtension;
import com.asierso.astra.models.Action;
import com.asierso.astra.models.Hook;
import com.asierso.astra.models.ScrapperManifest;
import com.asierso.astra.models.ActionTypes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asierso
 */
public class ModelLoader {

	private ScrapperEngine scrapper;
	private final ScrapperManifest scm;

	public ModelLoader(ScrapperManifest scm) {
		this.scm = scm;
	}

	public void run() throws FileNotFoundException, CloneNotSupportedException, HookException {
		// Start scrapping engine
		if (scm.getArgs() != null) {
			ArrayList<String> clArgs = new ArrayList<String>();
			clArgs.addAll(scm.getArgs());
			scrapper = new ScrapperEngine(clArgs);
		} else {
			scrapper = new ScrapperEngine(scm.getArgs());
		}
		System.out.println("Loading model " + scm.getName() + " ver: " + scm.getVersion());

		// Start executing model
		executeActions(scm.getActions(), null, null);

		System.out.println("Main actions loaded. Waiting hooks");

		// Hooks showcase
		System.out.print("Hooks loaded: | ");
		for (String obj : scm.getHooksNames()) {
			System.out.print(obj + " |");
		}
		System.out.println();
	}

	public void stop() {
		// Send stop to scrapper
		if (scrapper != null)
			scrapper.stop();
	}

	private List<String> executeActions(List<Action> actList, ArrayList<String> hookParameters, ArrayList<String> inneritedOutput)
			throws HookException, CloneNotSupportedException {
		// Output buffer
		ArrayList<String> output;
		if(inneritedOutput == null)
			output = new ArrayList<>();
		else
			output = inneritedOutput;

		// Secuence action list (actions or executed hook)
		for (Action handle : actList) {
			Action obj = handle.clone();

			// Replace output vars
			if (!obj.isNoOutput() && obj.getParameters() != null && !output.isEmpty())
				obj.setParameters(RegexExtension.replaceOutput(obj.getParameters(), output));
			if (!obj.isNoParams() && obj.getParameters() != null) 
				obj.setParameters(RegexExtension.replaceParameters(obj.getParameters(), hookParameters));

			// Action points to another hook
			if (obj.getType() == ActionTypes.HOOK) {
				output.addAll(executeHook(obj.getParameters(),
						hookParameters, (ArrayList<String>) obj.getBody()));
			} else {
				// Execute action
				output.add(scrapper.executeAction(obj));
			}
		}
		return output;
	}

	public List<String> executeHook(String hookName, ArrayList<String> parameters, ArrayList<String> inneritedOutput)
			throws HookException, CloneNotSupportedException {
		Hook selected = getHook(hookName);
		return executeActions(selected.getActions(), parameters, inneritedOutput);
	}

	public String executeHookWithOutput(String hookName, ArrayList<String> parameters, ArrayList<String> inneritedOutput)
			throws HookException, CloneNotSupportedException {
		return RegexExtension.replaceOutput(getHook(hookName).getOutput(), executeHook(hookName, parameters,inneritedOutput));
	}

	private Hook getHook(String hookName) throws HookException {
		// Find hook
		Hook selected = scm.getHooks().stream().filter((obj) -> obj.getName().equals(hookName)).findFirst().get();

		// Throws error if hook not exists
		if (selected == null) {
			throw new HookException("Hook not found");
		}
		return selected;
	}

	public ScrapperEngine getScrapper() {
		return scrapper;
	}

	public ScrapperManifest getScrapperManifest() {
		return scm;
	}
}
