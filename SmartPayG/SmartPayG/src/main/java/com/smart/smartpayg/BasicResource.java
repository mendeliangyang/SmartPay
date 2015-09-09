/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg;

import com.smart.common.DBHelper;
import com.smart.common.FormationResult;
import com.smart.common.SignVerify.SignCommon;
import com.smart.common.SignVerify.SignInformationModel;
import com.smart.common.UtileSmart;
import com.smart.common.model.ExecuteResultParam;
import com.smart.common.model.ResponseResultCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("Basic")
public class BasicResource {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of BasicResource
     */
    public BasicResource() {
    }

    @POST
    @Path("GetPayChannl")
    public String GetPayChannl(String param) {

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        List<Map<String, String>> resultList = null;
        String strSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
//            paramMap = new HashMap<String, Object>();
//
//            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

//            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
//            if (signModel == null) {
//                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
//            }
            strSql = "select * from PayChannel";

            resultList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);
            return formationResult.formationResult(ResponseResultCode.Success, "", new ExecuteResultParam(resultList, false));

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
           
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }
}
