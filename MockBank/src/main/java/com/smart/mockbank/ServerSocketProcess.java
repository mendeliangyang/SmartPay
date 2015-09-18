/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbank;

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

    public ServerSocketProcess(Socket pSocket) {
        this.socketClient = pSocket;
        try {
            outputStream = socketClient.getOutputStream();
            inputStream = socketClient.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (1 == 1) {
            try {
                byte[] readTempCharArray = new byte[1024];
                List<Byte> readTotalArray = new ArrayList<Byte>();
                int readTempLen = 0, readTotal = 0;
                while ((readTempLen = inputStream.read(readTempCharArray)) != -1) {
                    for (int i = 0; i < readTempLen; i++) {
                        readTotalArray.add(readTempCharArray[i]);
                    }
//                    System.arraycopy(readTempCharArray, 0, readTotalArray, readTotal, readTempCharArray.length);
                    readTotal += readTempLen;
                }

                //close inputStream  
                inputStream.close();
                //send data
                outputStream.write("data".getBytes());
                outputStream.flush();
                //close outputStream
                outputStream.close();

                //close socketClient
                socketClient.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
