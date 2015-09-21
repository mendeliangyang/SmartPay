/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbank;

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
                byte[] dataLen = new byte[4];
                int idataLen = 0;
                int readTempLen = 0, readTotal = 0;
                while ((readTempLen = inputStream.read(readTempCharArray)) != -1) {
//                    System.out.print(Arrays.toString(readTempCharArray));
                    System.out.println(Packet.bytesToHexString(readTempCharArray));
                    System.arraycopy(readTempCharArray, 0, dataLen, 0, 4);
//                    Packet.toLH(readTotal);
//                    idataLen = Packet.byte2int(dataLen);
                    String strDataLen = new String(dataLen);
                    idataLen = Integer.parseInt(strDataLen);
                    for (int i = 0; i < readTempLen; i++) {
                        readTotalArray.add(readTempCharArray[i]);
                    }
//                    System.arraycopy(readTempCharArray, 0, readTotalArray, readTotal, readTempCharArray.length);
                    readTotal += readTempLen;
                    if ((idataLen + 4) == readTotal) {
                        break;
                    }
                }

                //send data
                outputStream.write("data".getBytes());
                outputStream.flush();
                //close outputStream
                outputStream.close();
                //close inputStream  
                inputStream.close();
                //close socketClient
                socketClient.close();
                return;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
