
package com.asierso.astra;

import com.asierso.astra.sockets.Server;
import java.io.File;

/**
 *
 * @author Asierso
 */
public class Main {

    public static void main(String[] args) throws Exception {
    	int port = 26700;
    	
    	//Get arguments 
    	if(args.length > 0) {
    		try {
    			port = Integer.parseInt(args[0]); //Set new port
    		}catch(NumberFormatException e) {
    			System.out.println("Incorrect argument");
    		}
    	}
    	
        System.out.println("Astra - Scrapper server");

        File dir = new File("models");
        if (dir.exists() && dir.isDirectory()) { //Load models from directory
            System.out.println("Loading models");
            for(File f : dir.listFiles()){ //Load single model
                if(f.getName().endsWith(".json")){
                    System.out.println(" - Model: " + CacheModels.getInstance().loadModelManifest(new FileManager(f.getName())) + " (" + f.getName() + ")");
                }
            }
            new Server(port).run(); //Run server at specified port
        } else {
            //Create folder and exit
            dir.mkdir();
            System.out.println("Folder models created. Insert .json models and start server again");
        }
    }
}
