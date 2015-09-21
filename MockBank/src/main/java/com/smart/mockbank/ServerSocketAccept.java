/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbank;

import static com.smart.mockbank.Main.ClientProcessPool;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class ServerSocketAccept implements Runnable {

    public ServerSocketAccept(ServerSocket pServer) {
        this.server = pServer;
    }

    ServerSocket server = null;

    @Override
    public void run() {
        if (server == null) {
            return;
        }

        while (1 == 1) {
            Socket clientScoket = null;
            try {
                clientScoket = server.accept();
                ClientProcessPool.execute(new ServerSocketProcess(clientScoket));
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
