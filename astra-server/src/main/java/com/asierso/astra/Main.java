
package com.asierso.astra;

import com.asierso.astra.extensions.DigestExtension;
import com.asierso.astra.models.ScrapperManifest;
import com.asierso.astra.scrapper.ModelLoader;
import com.asierso.astra.sockets.Server;
import com.asierso.astracommons.TokenExtension;
import com.google.common.collect.Multiset.Entry;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Asierso
 */
public class Main {

	public static void main(String[] args) throws Exception {
		int port = 26700;
		boolean exitThen = false;

		// Get arguments
		if (args.length > 0) {
			// Get parameters order by parameter key
			HashMap<String, String> parameters = new HashMap();
			for (int i = 0; i < args.length; i++) {
				// Is a value and not a key
				if (!args[i].startsWith("-"))
					continue;

				// Process arguments (is key)
				if (i == args.length - 1 || (i + 1 < args.length && args[i + 1].startsWith("-")))
					parameters.put(args[i].substring(1), null); // add key without value
				else
					parameters.put(args[i].substring(1), args[i + 1]); // add key with value
			}

			// Map parameters list and apply to Astra configuration
			for (Map.Entry<String, String> handle : parameters.entrySet()) {
				try {
					switch (handle.getKey()) {
					case "p" -> port = Integer.parseInt(handle.getValue()); // Set new port
					case "t" -> { // Generate models with token
						File dir = new File("models");
						if (!dir.isDirectory() || !dir.exists())
							dir.mkdir();
						generateToken();
					}
					case "d" -> { // Revoke token
						File tokenFile = new File("models/.token");
						if (tokenFile.exists()) {
							tokenFile.delete();
							System.out.println("Token revoked. You can generate new one with -t arg");

						}
					}
					case "q" -> // Quit server when configuration parameters processed
						exitThen = true;
					case "test" -> { //Only testing purposes
						System.out.println("Test mode");
						ModelLoader loader = new ModelLoader(new Gson().fromJson(new FileManager("test.json").read(), ScrapperManifest.class));
						loader.run();
						ArrayList<String> p1 = new ArrayList<String>();
						p1.add("prueba");
						System.out.println(loader.executeHookWithOutput("test", p1, new ArrayList<String>()));
						exitThen = true;
					}
					default -> throw new Exception();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Incorrect argument name or value at arg: " + handle);
					exitThen = true;
				}
			}
		}

		if (exitThen) {
			System.out.println("Exitting");
			return;
		}

		System.out.println("Astra - Scrapper server");

		File dir = new File("models");
		if (dir.exists() && dir.isDirectory()) { // Load models from directory
			System.out.println("Loading models");
			for (File f : dir.listFiles()) { // Load single model
				if (f.getName().endsWith(".json")) {
					System.out.println(
							" - Model: " + CacheModels.getInstance().loadModelManifest(new FileManager(f.getName()))
									+ " (" + f.getName() + ")");
				}
			}
			new Server(port).run(); // Run server at specified port
		} else {
			// Create folder and exit
			dir.mkdir();
			System.out.println("Folder models created");

			// Generate token (opcional)
			Scanner sc = new Scanner(System.in);
			System.out.print("Generate access token (y/n)?: ");
			if (sc.nextLine().toLowerCase().charAt(0) == 'y') {
				generateToken();
			}
			System.out.println("Config success. Insert .json models and start server again");
		}
	}

	private static void generateToken() throws IOException {
		String token = TokenExtension.generateToken(25);
		FileManager tokenFile = new FileManager(".token");
		tokenFile.write(DigestExtension.toMD5(token));
		System.out.println("Token (copy from screen): " + token);
	}
}
