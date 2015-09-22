/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.netSocket;

import com.smart.common.RSLogger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class NetSocketCommon {

    static ExecutorService ClientProcessPool;

    static {
        ClientProcessPool = Executors.newCachedThreadPool();
    }

    public static byte[] Interactive(NetSocketCarrier carrier) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        Socket client = null;
        try {
            client = new Socket(carrier.strIp, carrier.iPort);
            outputStream = client.getOutputStream();

            outputStream.write(carrier.strMessage);
            outputStream.flush();

            inputStream = client.getInputStream();

            int onceReadLen = 0, totalReadLen = 0, realTotalLen = 0;
            byte[] byteRealTotalLen = new byte[carrier.iRealReadLen];
            //read data len
            onceReadLen = inputStream.read(byteRealTotalLen);
            totalReadLen += onceReadLen;
            realTotalLen = Integer.parseInt(new String(byteRealTotalLen));
            //read data len
            byte[] byteRead = new byte[realTotalLen];
            onceReadLen = inputStream.read(byteRead);
            totalReadLen += onceReadLen;
            return byteRead;
        } catch (IOException | NumberFormatException e) {
            RSLogger.ErrorLogInfo("Interactive error." + e.getLocalizedMessage(), e);
            return null;
        } finally {
            try {
                //close outputStream
                if (outputStream != null) {
                    outputStream.close();
                }
                //close inputStream  
                if (inputStream != null) {
                    inputStream.close();
                }
                //close socketClient
                if (client != null) {
                    client.close();
                }
            } catch (Exception e) {
                RSLogger.ErrorLogInfo("Interactive error. on close socket" + e.getLocalizedMessage(), e);
            }
        }
    }

}
