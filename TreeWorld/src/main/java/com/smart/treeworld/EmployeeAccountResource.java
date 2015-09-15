/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.treeworld;

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
@Path("EmployeeAccount")
public class EmployeeAccountResource {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    /**
     * Creates a new instance of EmployeeAccountResource
     */
    public EmployeeAccountResource() {
    }

    @POST
    @Path("SearchOverage")
    public String SearchOverage(String param) {

        String paramKey_AccountNum = "AccountNum";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        String strSql = null;
        List<Map<String, String>> resultList = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountNum, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

//            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
//            if (signModel == null) {
//                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
//            }
            strSql = String.format("SELECT emp_id,appstate,card_balance,charge_date,last_date FROM EmployeeAccount where card_id='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum));
            resultList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);

            return formationResult.formationResult(ResponseResultCode.Success, "", new ExecuteResultParam(resultList, false));

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

    @POST
    @Path("SearchConsumeRecord")
    public String SearchConsumeRecord(String param) {

        String paramKey_AccountNum = "AccountNum";
        String paramKey_SkipNum = "SkipNum";
        String paramKey_TopNum = "TopNum";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        String strSql = null, strSqlCount = null;
        int TopNum = 0, SkipNum = 20;
        List<Map<String, String>> resultList = null;
        String tempCount = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_AccountNum, null);
            paramMap.put(paramKey_SkipNum, null);
            paramMap.put(paramKey_TopNum, null);
            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            SkipNum = UtileSmart.tryGetIntFromMap(paramMap, paramKey_SkipNum);
            TopNum = UtileSmart.tryGetIntFromMap(paramMap, paramKey_TopNum);
            TopNum = TopNum == 0 ? 20 : TopNum;

//            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
//            if (signModel == null) {
//                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
//            }

            strSql = String.format("select top %d * from mealrecords  where emp_id=(select emp_id from EmployeeAccount where card_id='%s') and  (sign_time not in (select top %d sign_time from mealrecords order by sign_time)) order by sign_time", TopNum, UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum), SkipNum);
            strSqlCount = String.format("select count(*) from mealrecords where emp_id=(select emp_id from EmployeeAccount where card_id='%s')", UtileSmart.getStringFromMap(paramMap, paramKey_AccountNum));

            tempCount = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), strSqlCount);

            resultList = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), strSql);
            return formationResult.formationResult(ResponseResultCode.Success, "", new ExecuteResultParam(resultList, tempCount, true));

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

}
