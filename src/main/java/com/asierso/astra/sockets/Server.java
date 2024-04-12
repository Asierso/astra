
package com.asierso.astra.sockets;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author asier
 */
public class Server {

    private final int port;

    public Server(int port){
        this.port = port;
    }
    
    public void run() throws Exception {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Running server at port " + port);
        while (true) {
            Socket client = server.accept();
            System.out.println("Client connected: " + client.getInetAddress());

            //Attend client async
            Thread clientThread = new Thread(new ClientSocketManager(client));
            clientThread.start();
        }
    }

    public int getPort() {
        return port;
    }
}
