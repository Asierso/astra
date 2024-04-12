/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.asierso.astra;

import com.asierso.astra.sockets.Server;
import java.io.File;

/**
 *
 * @author asier
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Astra - Scrapper server");

        File dir = new File("models");
        if (dir.exists() && dir.isDirectory()) {
            System.out.println("Loading models");
            for(File f : dir.listFiles()){
                if(f.getName().endsWith(".json")){
                    System.out.println(" - Model: " + CacheModels.getInstance().loadModelManifest(new FileManager(f.getName())) + " (" + f.getName() + ")");
                }
            }
            new Server(26700).run(); //Run server
        } else {
            //Create folder and exit
            dir.mkdir();
            System.out.println("Folder models created. Insert .json models and start server again");
        }
    }
}
