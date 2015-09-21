/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.mockbank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Administrator
 */
public class Main {

    static ServerSocket server = null;
    static int serverPort = 4000;
    static ExecutorService ServerSinglePool;
    static ExecutorService ClientProcessPool;

    public static void main(String args[]) {
        try {
            ClientProcessPool = Executors.newCachedThreadPool();
            
            ServerSinglePool = Executors.newSingleThreadExecutor();
            server = new ServerSocket(serverPort);

            ServerSocketAccept ssa = new ServerSocketAccept(server);
            ServerSinglePool.execute(ssa);

//            Socket clientScoket = null;
//            clientScoket = server.accept();
//
//            String line;
//            BufferedReader is = new BufferedReader(new InputStreamReader(clientScoket.getInputStream()));
//
//            PrintWriter os = new PrintWriter(clientScoket.getOutputStream());
//
//            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
//            //由系统标准输入设备构造BufferedReader对象
//
//            System.out.println("Client:" + is.readLine());
//            //在标准输出上打印从客户端读入的字符串
//
//            line = sin.readLine();
//            //从标准输入读入一字符串
//
//            while (!line.equals("bye")) {
//                //如果该字符串为 "bye"，则停止循环
//
//                os.println(line);
//                //向客户端输出该字符串
//
//                os.flush();
//                //刷新输出流，使Client马上收到该字符串
//
//                System.out.println("Server:" + line);
//                //在系统标准输出上打印读入的字符串
//
//                System.out.println("Client:" + is.readLine());
//                //从Client读入一字符串，并打印到标准输出上
//
//                line = sin.readLine();
//                //从系统标准输入读入一字符串
//
//            } //继续循环
//
//            os.close(); //关闭Socket输出流
//
//            is.close(); //关闭Socket输入流
//
//            clientScoket.close(); //关闭Socket
//
//            server.close(); //关闭ServerSocket
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

    }

}
