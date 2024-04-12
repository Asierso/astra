
package com.asierso.astra.sockets;

import com.asierso.astra.CacheModels;
import com.asierso.astra.exceptions.ModelLoadException;
import com.asierso.astra.exceptions.RequestException;
import com.asierso.astra.extensions.RegexExtension;
import com.asierso.astra.models.requests.ClientRequest;
import com.asierso.astra.models.requests.ClientResponse;
import com.asierso.astra.scrapper.ModelLoader;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author asier
 */
public class ClientSocketManager implements Runnable {

    private final Socket client;
    private boolean running;

    public ClientSocketManager(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        //Buffered models
        HashMap<String, ModelLoader> models = new HashMap<>();
        try {
            //Socket input and output 
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            running = true;

            String buffer;
            while ((buffer = in.readLine()) != null && running) {
                ClientRequest req = new Gson().fromJson(buffer, ClientRequest.class);
                ClientResponse res = new ClientResponse();

                //Buffered model
                ModelLoader model = null;

                if (req != null) { //Process request if is not null
                    try {
                        switch (req.getAction()) {
                            case MODEL -> { //Load model
                                if (models.containsKey(req.getModel())) {
                                    throw new RequestException(400, "Model already launched");
                                }

                                logStatus(0, "Starting model " + req.getModel());

                                //Create model loader and run it
                                model = new ModelLoader(CacheModels.getInstance().getModelManifest(req.getModel()));
                                model.run();
                                models.put(req.getModel(), model);
                                
                                res.setStatus(200); 
                            }
                            case MODEL_LIST -> { //Load list of models
                                res.setStatus(200);
                                res.setOutput(CacheModels.getInstance().getModelList());
                                
                                logStatus(0, "Loading model list");
                            }
                            case HOOK -> { //Hook execution
                                model = searchModel(req.getModel(), models);

                                logStatus(0, "Running hook " + req.getHook());

                                //Execute hook and replace output vars
                                List<String> output = model.executeHook(req.getHook(), (ArrayList<String>) req.getParameters());
                                res.setStatus(200);
                                res.setBody(RegexExtension.replaceVars(model.getScrapperManifest()
                                        .getHooks().stream().filter(obj -> obj.getName().equals(req.getHook()))
                                        .findFirst().get().getOutput(), output));
                                res.setOutput(output);
                            }
                            case HOOK_LIST -> {
                            	model = searchModel(req.getModel(), models);
                            	
                            	//Get hooks name
                            	logStatus(0, "Getting hooks for model" + req.getModel());
                            	res.setStatus(200);
                            	res.setOutput(model.getScrapperManifest().getHooksNames());
                            }
                            case EXIT -> { //Close scrapper
                                logStatus(0, "Exiting " + client.getInetAddress());
                                running = false;
                            }
                            default ->
                                throw new RequestException(400, "Incorrect request");
                        }
                    } catch (RequestException e) { //Error in client request
                        res.setStatus(e.getStatus());
                        res.setBody(e.getMessage());
                        logStatus(e.getStatus(), e.getMessage());
                    } catch (ModelLoadException e) { //Trying to use a non-existant model
                        res.setStatus(500);
                        res.setBody("Model not found");
                        logStatus(500, "Model not found");
                    } catch (CloneNotSupportedException e) {
                    	logStatus(0, "Internal error " + e.getMessage());
                    }
                }
                //Return client response
                out.println(new Gson().toJson(res));
            }
            client.close();
            
            //Client exited
            stopModels(models);
            logStatus(0,"Stopping buffered models");
        } catch (Exception e) {
            logStatus(500,"Stopping buffered models (Error) " + e.getMessage());
            stopModels(models);
        }
    }

    private ModelLoader searchModel(String modelName, HashMap<String, ModelLoader> models) throws RequestException {
        if (!models.containsKey(modelName)) {
            throw new RequestException(400, "Model must be launched before");
        }
        return models.get(modelName);
    }

    private void stopModels(HashMap<String, ModelLoader> models) {
        for (Map.Entry<String, ModelLoader> handle : models.entrySet()) {
            handle.getValue().stop();
        }
    }

    private void logStatus(int status, String message) {
        System.out.println("Client " + client.getInetAddress() + " - Status " + status + ": " + message);
    }
}
