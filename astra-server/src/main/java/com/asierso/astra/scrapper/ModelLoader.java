
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
		executeActions(scm.getActions(), null);

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

	private List<String> executeActions(List<Action> actList, ArrayList<String> hookParameters)
			throws HookException, CloneNotSupportedException {
		// Output buffer
		ArrayList<String> output = new ArrayList<>();

		// Secuence action list (actions or executed hook)
		for (Action handle : actList) {
			Action obj = handle.clone();

			// Replace output vars
			if (obj.isUseoutput()) {
				obj.setParameters(RegexExtension.replaceVars(obj.getParameters(), output));
			} else if (obj.isUseparameters()) {
				obj.setParameters(RegexExtension.replaceVars(obj.getParameters(), hookParameters));
			}

			// Action points to another hook
			if (obj.getType() == ActionTypes.HOOK) {
				output.addAll(executeHook(obj.getParameters(),
						obj.isUseparameters() ? hookParameters : (ArrayList<String>) obj.getBody()));
			} else {
				// Execute action
				output.add(scrapper.executeAction(obj));
			}
		}
		return output;
	}

	public List<String> executeHook(String hookName, ArrayList<String> parameters)
			throws HookException, CloneNotSupportedException {
		Hook selected = getHook(hookName);
		return executeActions(selected.getActions(), parameters);
	}

	public String executeHookWithOutput(String hookName, ArrayList<String> parameters)
			throws HookException, CloneNotSupportedException {
		return RegexExtension.replaceVars(getHook(hookName).getOutput(), executeHook(hookName, parameters));
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
