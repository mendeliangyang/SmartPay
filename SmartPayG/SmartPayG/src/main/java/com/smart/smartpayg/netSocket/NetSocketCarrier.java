/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.netSocket;

/**
 *
 * @author Administrator
 */
public class NetSocketCarrier {

    public String strIp;
    public int iPort;
    public int iRealReadLen = 4; //接受返回消息，读取消息长度
//    public int iReckonReadLen = 512; //估计返回的数据长度
    public byte[] strMessage; //要发送的消息

}
