
package com.asierso.astra;

import com.asierso.astra.extensions.DigestExtension;
import com.asierso.astra.sockets.Server;
import com.asierso.astracommons.TokenExtension;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Asierso
 */
public class Main {

	public static void main(String[] args) throws Exception {
		int port = 26700;
		
		// Get arguments
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]); // Set new port
			} catch (NumberFormatException e) {
				System.out.println("Incorrect argument");
			}
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
				String token = TokenExtension.generateToken(25);
				FileManager tokenFile = new FileManager(".token");
				tokenFile.write(DigestExtension.toMD5(token));
				
				System.out.println("Token (copy from screen): " + token);
			}
			System.out.println("Config success. Insert .json models and start server again");
		}
	}
}
