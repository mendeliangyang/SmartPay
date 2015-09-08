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
import java.util.ArrayList;
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
@Path("AccountBind")
public class AccountBindResource {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of AccountBindResource
     */
    public AccountBindResource() {
    }

    @POST
    @Path("BindAccount")
    public String BindAccount(String param) {

        String paramKey_FromAccountId = "FromAccountId";
        String paramKey_ToAccountId = "ToAccountId";
        String paramKey_VerifyCentext = "VerifyCentext";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        String strSql = null;
        String accountBindId = null;
        SignInformationModel signModel = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_FromAccountId, null);
            paramMap.put(paramKey_ToAccountId, null);
            paramMap.put(paramKey_VerifyCentext, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            accountBindId = UtileSmart.getUUID();

            strSql = String.format("insert into AccountBind (AccountBindId,FromAccountId,ToAccountId,UserId,PutTime) values ('%s','%s','%s','%s',getdate())", accountBindId, UtileSmart.getStringFromMap(paramMap, paramKey_FromAccountId), UtileSmart.getStringFromMap(paramMap, paramKey_ToAccountId), signModel.Id);
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
    @Path("InvalidMyAccountBind")
    public String InvalidMyAccountBind(String param) {

        String paramKey_AccountBindId = "AccountBindId";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        List<String> listSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountBindId, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            listSql = new ArrayList<>();
            listSql.add(String.format("update AccountBind set InvalidTime=getdate() where AccountBindId='%s' and UserId='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), signModel.Id));
            listSql.add(String.format("insert into AccountBindLS select * from AccountBind where AccountBindId='%s' and UserId='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), signModel.Id));
            listSql.add(String.format("delete AccountBind where AccountBindId='%s' and UserId='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), signModel.Id));

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
    @Path("SelectMyAccountBind")
    public String SelectMyAccountBind(String param) {

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
            strSql = String.format("SELECT a.*,mf.AccountNum as FAcountNum,mf.AccountType as FAccountType,mf.MasterRealName as FMasterRealName,mf.MasterVerifyPhone as FMasterVerifyPhone ,mt.AccountNum as TAcountNum,mt.AccountType as TAccountType,mt.MasterRealName as TMasterRealName,mt.MasterVerifyPhone as TMasterVerifyPhone FROM AccountBind as a left join MyAccount as mf on a.FromAccountId=mf.AccountId left join MyAccount as mt on a.ToAccountId = mt.AccountId where a.UserId='%s'", signModel.Id);

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
