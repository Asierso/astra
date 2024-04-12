/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.asierso.astra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author asier
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
