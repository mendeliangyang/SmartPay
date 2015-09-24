/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.netSocket;

import com.smart.common.model.SmartDecodingEnum;
import java.nio.charset.Charset;

/**
 *
 * @author Administrator
 */
public class ShiftPayCarrier {

    public ShiftPayCarrier() {
        version = "V001".toCharArray();
        handleCode = "600005".toCharArray();
        branchNum = "138000".toCharArray();
        terminalNum = "99999999".toCharArray();
        terminalDate = "20150922".toCharArray();
        terminalTime = "104300".toCharArray();
        mcenterNum = "00038702".toCharArray();
        mcenterBranchNum = "138070".toCharArray();
        dealType = "0502".toCharArray();
        appCode = "....".toCharArray();
        mac = "FFFFFFFF".toCharArray();
    }

    public byte[] toData() {
        StringBuffer sb = new StringBuffer();
        int dataLen = version.length + handleCode.length + branchNum.length + terminalNum.length + terminalDate.length + terminalTime.length + mcenterNum.length + mcenterBranchNum.length + payBankNum.length + shiftPayNum.length
                + shiftPayaName.length + shiftPayMoeny.length + dealType.length + appCode.length + bankType.length + signFlag.length + dealFlag.length + sMcenterDate.length + sTerminalDate.length + sBankLog.length + phoneNum.length
                + mac.length;
        sb.append(String.format("%04d", dataLen));
        sb.append(version).append(handleCode).append(handleCode).append(branchNum).append(terminalNum).append(terminalDate).append(terminalTime).append(mcenterNum).append(mcenterBranchNum).append(payBankNum)
                .append(shiftPayNum).append(shiftPayaName).append(shiftPayMoeny).append(dealType).append(appCode).append(bankType).append(signFlag).append(dealFlag).append(sMcenterDate).append(sTerminalDate)
                .append(sBankLog).append(phoneNum).append(mac);
        return sb.toString().getBytes(Charset.forName(SmartDecodingEnum.ascii.toString()));
    }

    public char[] version = new char[4];
    public char[] handleCode = new char[6];
    public char[] branchNum = new char[6];
    public char[] terminalNum = new char[8];
    public char[] terminalDate = new char[8];
    public char[] terminalTime = new char[6];
    public char[] mcenterNum = new char[8];
    public char[] mcenterBranchNum = new char[6];
    public char[] payBankNum = new char[20];
    public char[] shiftPayNum = new char[32];
    public char[] shiftPayaName = new char[60];
    public char[] shiftPayMoeny = new char[12];
    public char[] dealType = new char[4];
    public char[] appCode = new char[4];
    public char[] bankType = new char[1];
    public char[] signFlag = new char[1];
    public char[] dealFlag = new char[1];
    public char[] sMcenterDate = new char[8];
    public char[] sTerminalDate = new char[8];
    public char[] sBankLog = new char[6];
    public char[] phoneNum = new char[11];
    public char[] mac = new char[8];

}
