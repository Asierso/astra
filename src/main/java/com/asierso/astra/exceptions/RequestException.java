/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.asierso.astra.exceptions;

/**
 *
 * @author sobblaas
 */
public class RequestException extends Exception{
    private int status;
    public RequestException(int status,String text){
        super(text);
        this.status = status;
    }
    
    public int getStatus() {
        return status;
    }
    
}
