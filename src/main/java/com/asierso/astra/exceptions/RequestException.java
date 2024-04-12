
package com.asierso.astra.exceptions;

/**
 *
 * @author Asierso
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
