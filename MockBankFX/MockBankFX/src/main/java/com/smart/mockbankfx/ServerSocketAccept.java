/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbankfx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class ServerSocketAccept implements Runnable {

    public ServerSocketAccept(ServerSocket pServer, FXMLController pController) {
        this.server = pServer;
        controller = pController;
    }

    ServerSocket server = null;
    FXMLController controller = null;

    @Override
    public void run() {
        if (server == null) {
            return;
        }

        while (1 == 1) {
            Socket clientScoket = null;
            try {

                controller.SetMessage("service ready accept client.");
                clientScoket = server.accept();
                FXMLController.ClientProcessPool.execute(new ServerSocketProcess(clientScoket, controller));
                controller.SetMessage("client accept");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
