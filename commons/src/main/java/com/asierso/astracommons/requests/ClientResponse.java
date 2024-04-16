
package com.asierso.astracommons.requests;

import java.util.List;

/**
 *
 * @author Asierso
 */
public class ClientResponse {
    private int status;
    private String body;
    private List<String> output;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }
    
        
}
