
package com.asierso.astra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Asierso
 */
public class FileManager {

    private final String source;
    private String content;

    public FileManager(String source) {
        this.source = "models/" + source;
    }

    public String read() throws FileNotFoundException {
        if (this.content == null || this.content.isBlank()) {
            StringBuilder contBuild = new StringBuilder();
            try (Scanner sc = new Scanner(new File(source))) {
                while (sc.hasNextLine()) {
                    contBuild.append(sc.nextLine());
                }
            }
            this.content = contBuild.toString();
        }
        return this.content;
    }
    
    public boolean exists() {
    	return new File(source).exists();
    }
    
    public String readByLines() throws FileNotFoundException {
    	if (this.content == null || this.content.isBlank()) {
            StringBuilder contBuild = new StringBuilder();
            try (Scanner sc = new Scanner(new File(source))) {
                while (sc.hasNextLine()) {
                    contBuild.append(sc.nextLine() + "\n");
                }
            }
            this.content = contBuild.toString();
        }
        return this.content;
    }
    
    public void write(String data) throws IOException{
        try (FileWriter fw = new FileWriter(source)) {
            fw.write(data);
        }
        this.content = data;
    }
    
    public void flush(){
        this.content = null;
    }
}
