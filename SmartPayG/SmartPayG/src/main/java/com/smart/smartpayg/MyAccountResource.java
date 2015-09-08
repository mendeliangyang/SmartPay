/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartpayg;

import com.smart.common.DBHelper;
import com.smart.common.DeployInfo;
import com.smart.common.FormationResult;
import com.smart.common.SignVerify.SignCommon;
import com.smart.common.SignVerify.SignInformationModel;
import com.smart.common.UtileSmart;
import com.smart.common.model.ExecuteResultParam;
import com.smart.common.model.ResponseResultCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("MyAccount")
public class MyAccountResource {

    @Context
    private UriInfo context;

    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of MyAccountResource
     */
    public MyAccountResource() {
    }

    @POST
    @Path("PutMyAccount")
    public String PutMyAccount(String param) {

        String paramKey_AccountNum = "AccountNum";
        String paramKey_AccountType = "AccountType";
        String paramKey_MasterRealName = "MasterRealName";
        String paramKey_MasterVerifyPhone = "MasterVerifyPhone";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        String strSql = null;
        String strAccountId = null;
        String selectReultStr = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountNum, null);
            paramMap.put(paramKey_AccountType, null);
            paramMap.put(paramKey_MasterRealName, null);
            paramMap.put(paramKey_MasterVerifyPhone, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            //同账户号不能多次添加，不同的用户可以添加同一个账户。
            //验证用户名是否正确
            strSql = String.format("select AccountId from MyAccount where AccountNum='%s' and UserId='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), signModel.Id);
            selectReultStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), strSql);
            if (selectReultStr != null) {
                return formationResult.formationResult(ResponseResultCode.ErrorExistAccount, new ExecuteResultParam("账号已存在", param));
            }
            //需要调用第三分接口验证帐号信息是否正确
            strAccountId = UtileSmart.getUUID();
            strSql = String.format("insert into MyAccount (AccountId,AccountNum,AccountType,UserId,MasterRealName,MasterVerifyPhone,PayStatus,PutTime)values('%s','%s',%s,'%s','%s','%s',1,getdate())", strAccountId, UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), UtileSmart.getStringFromMap(paramMap, paramKey_AccountType), signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_MasterRealName), UtileSmart.getStringFromMap(paramMap, paramKey_MasterVerifyPhone));
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), strSql);
            if (resultParam.ResultCode >= 0) {

                JSONObject resultJson = new JSONObject();
                resultJson.accumulate("AccountId", strAccountId);
                if (resultParam.ResultJsonObject == null) {
                    resultParam.ResultJsonObject = new JSONObject();
                }
                resultParam.ResultJsonObject.accumulate(DeployInfo.ResultDataTag, resultJson);
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));

            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

    @POST
    @Path("ModifyMyAccount")
    public String ModifyMyAccount(String param) {

        String paramKey_AccountNum = "AccountNum";
        String paramKey_AccountType = "AccountType";
        String paramKey_MasterRealName = "MasterRealName";
        String paramKey_MasterVerifyPhone = "MasterVerifyPhone";
        String paramKey_AccountId = "AccountId";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        String strSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountNum, null);
            paramMap.put(paramKey_AccountType, null);
            paramMap.put(paramKey_MasterRealName, null);
            paramMap.put(paramKey_MasterVerifyPhone, null);
            paramMap.put(paramKey_AccountId, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            //同账户号不能多次添加，不同的用户可以添加同一个账户。
            //需要调用第三分接口验证帐号信息是否正确
            strSql = String.format("update MyAccount set AccountNum='%s' , AccountType=%s,MasterRealName='%s',MasterVerifyPhone='%s' where UserId='%s' and AccountId='%s'",
                    UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), UtileSmart.getStringFromMap(paramMap, paramKey_AccountType), UtileSmart.getStringFromMap(paramMap, paramKey_MasterRealName), UtileSmart.getStringFromMap(paramMap, paramKey_MasterVerifyPhone), signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_AccountId));
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), strSql);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

    @POST
    @Path("InvalidMyAccount")
    public String InvalidMyAccount(String param) {

        String paramKey_AccountNum = "AccountNum";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        List<String> listSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountNum, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            listSql = new ArrayList<>();
            listSql.add(String.format("update MyAccount set PayStatus=2, InvalidTime=getdate() where AccountNum='%s' and UserId='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), signModel.Id));
            listSql.add(String.format("insert into MyAccountLS select * from MyAccount  where AccountNum='%s' and UserId='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), signModel.Id));
            listSql.add(String.format("delete MyAccount where  AccountNum='%s' and UserId='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), signModel.Id));

            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), listSql);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

    @POST
    @Path("SelectMyAccount")
    public String SelectMyAccount(String param) {

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        List<Map<String, String>> resultList = null;
        String strSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            strSql = String.format("SELECT * FROM MyAccount where UserId='%s'", signModel.Id);

            resultList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);
            return formationResult.formationResult(ResponseResultCode.Success, signModel.token, new ExecuteResultParam(resultList, false));

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

}
