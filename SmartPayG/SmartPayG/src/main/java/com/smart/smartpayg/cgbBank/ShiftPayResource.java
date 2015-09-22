/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg.cgbBank;

import com.smart.common.DBHelper;
import com.smart.common.DatagramCoder;
import com.smart.common.FormationResult;
import com.smart.common.SignVerify.SignCommon;
import com.smart.common.SignVerify.SignInformationModel;
import com.smart.common.UtileSmart;
import com.smart.common.model.ExecuteResultParam;
import com.smart.common.model.ResponseResultCode;
import com.smart.smartpayg.SmartPayAnalyzeParam;
import com.smart.smartpayg.netSocket.ShiftPayCarrier;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.camel.ProducerTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("ShiftPay")
public class ShiftPayResource extends CodeExecution {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of ShiftPayResource
     */
    public ShiftPayResource() {
    }

    @POST
    @Path("tt")
    public String SearchOverage(String param) {
        try {
            ProducerTemplate template = getCamelContext().createProducerTemplate();
            Object answer = null;
            Object body = null;
            try {
                answer = template.requestBody("direct:shiftPayDirect", body);
            } finally {
                template.stop();
            }
            return answer == null ? "null" : answer.toString();
        } catch (Exception e) {
            return e.getLocalizedMessage();
        } finally {

        }
    }

    @POST
    @Path("t2")
    public String t2(String param) {
        String paramKey_AccountBindId = "AccountBindId";
        String paramKey_Money = "Money";
        String paramKey_VerifyContext = "VerifyContext";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        List<Map<String, String>> resultToList = null;
        List<Map<String, String>> resultFromList = null;
        try {
            paramMap = new HashMap<String, Object>();
            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }

            //验证手机验证码
            //调用银行接口判断是否签约
            //传入参数为卡绑定关系
            //根据卡的关系查询充值卡的信息
            String strSql = String.format("select * from MyAccount where AccountId=(select ToAccountId from AccountBind  where AccountBindId='%s')", UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId));
            resultToList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);
            if (resultToList == null || resultToList.isEmpty()) {
                return "error :充值卡未找到";
            }
            //查询银行卡的信息
            strSql = String.format("select * from MyAccount where AccountId=(select FromAccountId from AccountBind  where AccountBindId='%s')", UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId));
            resultFromList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);
            if (resultFromList == null || resultFromList.isEmpty()) {
                return "error :银行卡未找到";
            }
            //填充发送的消息内容
            ShiftPayCarrier payCarrier = new ShiftPayCarrier();

            payCarrier.payBankNum = DatagramCoder.padRight(resultToList.get(0).get(""), payCarrier.payBankNum.length, '0').toCharArray();
            payCarrier.shiftPayNum = DatagramCoder.padRight(resultToList.get(0).get(""), payCarrier.shiftPayNum.length, '0').toCharArray();
            payCarrier.shiftPayaName = DatagramCoder.padRight(resultToList.get(0).get(""), payCarrier.shiftPayaName.length, '0').toCharArray();
            payCarrier.shiftPayMoeny = DatagramCoder.padRight(UtileSmart.getStringFromMap(paramMap, paramKey_Money), '0').toCharArray();
            payCarrier.dealType = DatagramCoder.padRight("2000", payCarrier.dealType.length, '0').toCharArray();
            payCarrier.appCode = DatagramCoder.padRight("0002", payCarrier.appCode.length, '0').toCharArray();
            payCarrier.bankType = DatagramCoder.padRight("2", payCarrier.bankType.length, '0').toCharArray(); //1-贷记卡 2-借记卡
            payCarrier.signFlag = DatagramCoder.padRight("1", payCarrier.signFlag.length, '0').toCharArray();
            payCarrier.dealFlag = DatagramCoder.padRight("0", payCarrier.dealFlag.length, '0').toCharArray();
            payCarrier.phoneNum = DatagramCoder.padRight(resultToList.get(0).get(""), payCarrier.phoneNum.length, '0').toCharArray();
            
            //发送扣款
            //判断扣款状态，如果返回码 02需要冲正
            //写入充值记录
            return null;
        } catch (Exception e) {
            return e.getLocalizedMessage();
        } finally {

        }
    }

}
