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
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("ActOn")
public class ActOnResource {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of ActOnResource
     */
    public ActOnResource() {
    }

    @POST
    @Path("Consume")
    public String ActOn(String param) {

        String paramKey_AccountBindId = "AccountBindId";
        String paramKey_Money = "Money";
        String paramKey_VerifyContext = "VerifyContext";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        String actOnId = null;
        SignInformationModel signModel = null;
        List<String> listSql = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountBindId, null);
            paramMap.put(paramKey_Money, null);
            paramMap.put(paramKey_VerifyContext, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            listSql = new ArrayList<String>();
            actOnId = UtileSmart.getUUID();

            listSql.add(String.format("insert into ActOn (ActOnId,AccountBindId,Money,LogOnDateTime,ActOnStatus,UserId)values('%s','%s',%s,getdate(),1,'%s')",
                    actOnId, UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), UtileSmart.getStringFromMap(paramMap, paramKey_Money), signModel.Id));
            listSql.add(String.format("update ActOn set FromAccountId = (SELECT FromAccountId FROM AccountBind where AccountBindId='%s') where ActOnId='%s'",
                    UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), actOnId));
            listSql.add(String.format("update ActOn set ToAccountId = (SELECT ToAccountId FROM AccountBind where AccountBindId='%s') where ActOnId='%s'",
                    UtileSmart.getStringFromMap(paramMap, paramKey_AccountBindId), actOnId));

            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), listSql);
            if (resultParam.ResultCode >= 0) {
                JSONObject resultJson = new JSONObject();
                resultJson.accumulate("ActOnId", actOnId);
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
    @Path("SelectActOnRecord")
    public String SelectActOnRecord(String param) {

        String paramKey_SkipNum = "SkipNum";
        String paramKey_TopNum = "TopNum";
        String paramKey_ActOnStatus = "ActOnStatus";
        String paramKey_From = "From";
        String paramKey_To = "To";

        int SkipNum = 0;
        int TopNum = 20;

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        String strSql = null, strSqlCount = null;
        String tempWhere = null;
        List<Map<String, String>> listMap = new ArrayList<>();
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_SkipNum, null);
            paramMap.put(paramKey_TopNum, null);
            paramMap.put(paramKey_ActOnStatus, null);
            paramMap.put(paramKey_From, null);
            paramMap.put(paramKey_To, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            SkipNum = UtileSmart.tryGetIntFromMap(paramMap, paramKey_SkipNum);
            TopNum = UtileSmart.tryGetIntFromMap(paramMap, paramKey_TopNum);
            TopNum = TopNum == 0 ? 20 : TopNum;
            StringBuffer sbSql = new StringBuffer();
            StringBuffer sbSqlCount = new StringBuffer();
            strSql = String.format("select * ,sybid=identity(12) into #temp_actOn from ActOn select * from  #temp_actOn where UserId='%s' and sybid>%d and sybid<=%d ", signModel.Id, SkipNum, TopNum);
            strSqlCount = String.format("select count(*) from ActOn where UserId='%s'", signModel.Id);
            sbSql.append(strSql);
            sbSqlCount.append(strSqlCount);
            tempWhere = UtileSmart.tryGetStringFromMap(paramMap, paramKey_ActOnStatus);
            if (tempWhere != null && !tempWhere.isEmpty()) {
                sbSql.append(String.format(" and ActOnStatus=%s", tempWhere));
                sbSqlCount.append(String.format(" and ActOnStatus=%s", tempWhere));
            }
            tempWhere = UtileSmart.tryGetStringFromMap(paramMap, paramKey_From);
            if (tempWhere != null && !tempWhere.isEmpty()) {
                sbSql.append(String.format(" and FromAccountId='%s'", tempWhere));
                sbSqlCount.append(String.format(" and FromAccountId='%s'", tempWhere));
            }
            tempWhere = UtileSmart.tryGetStringFromMap(paramMap, paramKey_To);
            if (tempWhere != null && !tempWhere.isEmpty()) {
                sbSql.append(String.format(" and ToAccountId='%s'", tempWhere));
                sbSqlCount.append(String.format(" and ToAccountId='%s'", tempWhere));
            }

            sbSql.append("order by ActOnDateTime desc");
            tempWhere = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sbSqlCount.toString());

            listMap = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), sbSql.toString());
            return formationResult.formationResult(ResponseResultCode.Success, signModel.token, new ExecuteResultParam(listMap, tempWhere, true));
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap, listMap);
        }
    }

}
