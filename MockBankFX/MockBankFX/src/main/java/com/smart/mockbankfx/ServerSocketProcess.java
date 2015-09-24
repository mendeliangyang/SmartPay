/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbankfx;

import com.smart.common.DataFormat.Packet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ServerSocketProcess implements Runnable {

    private Socket socketClient = null;

    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    FXMLController controller = null;

    public ServerSocketProcess(Socket pSocket, FXMLController pController) {
        this.socketClient = pSocket;
        try {
            outputStream = socketClient.getOutputStream();
            inputStream = socketClient.getInputStream();
            controller = pController;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (1 == 1) {
            try {

                int onceReadLen = 0, totalReadLen = 0, realTotalLen = 0;
                byte[] byteRealTotalLen = new byte[4];
                //read data len
                onceReadLen = inputStream.read(byteRealTotalLen);
                controller.SetMessage("dataLen: " + Packet.bytesToHexString(byteRealTotalLen));
                totalReadLen += onceReadLen;
                realTotalLen = Integer.parseInt(new String(byteRealTotalLen));
                //read data len
                byte[] byteRead = new byte[realTotalLen];
                onceReadLen = inputStream.read(byteRead);
                totalReadLen += onceReadLen;
                controller.SetMessage("receive: " + Packet.bytesToHexString(byteRead));
                //send data
                outputStream.write("0004data".getBytes());
                outputStream.flush();
                controller.SetMessage("send:data");
                //close outputStream
                outputStream.close();
                //close inputStream  
                inputStream.close();
                //close socketClient
                socketClient.close();
                controller.SetMessage("socket close.");
                return;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
