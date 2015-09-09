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
@Path("Auth")
public class AuthResource {

    @Context
    private UriInfo context;
    SmartPayAnalyzeParam smartPayAnalyzeParam = new SmartPayAnalyzeParam();
    FormationResult formationResult = new FormationResult();

    public AuthResource() {
    }

    @POST
    @Path("GetVerifyCode")
    public String GetVerifyCode(String param) {

        String paramKey_VerifyPhone = "VerifyPhone";

        ExecuteResultParam resultParam = null;
        Map<String, Object> paramMap = null;

        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_VerifyPhone, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            JSONObject resultJson = new JSONObject();
            resultJson.accumulate("VerifyCode", "9999");
            resultParam = new ExecuteResultParam();
            if (resultParam.ResultJsonObject == null) {
                resultParam.ResultJsonObject = new JSONObject();
            }
            resultParam.ResultJsonObject.accumulate(DeployInfo.ResultDataTag, resultJson);
            return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, paramMap);
        }
    }

    @POST
    @Path("CheckUserNameUsed")
    public String CheckUserNameUsed(String param) {

        String paramKey_UserName = "UserName";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;

        List<String> sqlList = null;

        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_UserName, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            sqlStr = String.format("SELECT UserId FROM UserDetail where UserName='%s'",
                    UtileSmart.getStringFromMap(paramMap, paramKey_UserName));

            selectReultStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sqlStr);

            JSONObject resultJson = new JSONObject();

            //获取查询到数据
            if (selectReultStr == null) {
                resultJson.accumulate("IsUsed", "false");
            } else {
                resultJson.accumulate("IsUsed", "true");
            }
            resultParam = new ExecuteResultParam();
            if (resultParam.ResultJsonObject == null) {
                resultParam.ResultJsonObject = new JSONObject();
            }
            resultParam.ResultJsonObject.accumulate(DeployInfo.ResultDataTag, resultJson);
            return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("SignUp")
    public String SignUp(String param) {

        String paramKey_UserName = "UserName";
        String paramKey_Password = "Password";
        String paramKey_VerifyPhone = "VerifyPhone";
        String paramKey_VerifyCode = "VerifyCode";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;

        List<String> sqlList = null;

        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_UserName, null);
            paramMap.put(paramKey_Password, null);
            paramMap.put(paramKey_VerifyPhone, null);
            paramMap.put(paramKey_VerifyCode, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            //判断verifyCode，
            //注册码无效，返回错误
            //验证用户名是否正确
            sqlStr = String.format("select UserId from UserDetail where UserName='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_UserName));
            selectReultStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (selectReultStr != null) {
                return formationResult.formationResult(ResponseResultCode.ErrorExistUser, new ExecuteResultParam("用户名已经存在。", param));
            }
            //进行注册
            String userId = UtileSmart.getUUID();
            sqlStr = String.format("insert into UserDetail (UserId,UserName,Password,VerifyPhone)values('%s','%s','%s','%s')", userId, UtileSmart.getStringFromMap(paramMap, paramKey_UserName), UtileSmart.getStringFromMap(paramMap, paramKey_Password), UtileSmart.getStringFromMap(paramMap, paramKey_VerifyPhone));

            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (resultParam.ResultCode >= 0) {
                JSONObject resultJson = new JSONObject();
                resultJson.accumulate("UserId", userId);
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
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("SignIn")
    public String SignIn(String param) {

        String paramKey_Password = "Password";
        String paramKey_UserName = "UserName";

        ExecuteResultParam resultParam = null;
        String sqlStr = null;
        Map<String, Object> paramMap = null;
        Map<String, Map<String, String>> dbResultMap = null;

        List<String> sqlList = null;

        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_Password, null);
            paramMap.put(paramKey_UserName, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);

            //获取用户信息
            sqlStr = String.format("SELECT * FROM UserDetail where UserName='%s' and Password='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_UserName), UtileSmart.getStringFromMap(paramMap, paramKey_Password));

            dbResultMap = DBHelper.ExecuteSqlSelectReturnMap(smartPayAnalyzeParam.getRSID(), sqlStr, "UserDetail");
            if (dbResultMap == null || dbResultMap.size() == 0) {
                //密码错误，提示密码错误
                return formationResult.formationResult(ResponseResultCode.ErrorUserOrPwd, new ExecuteResultParam("用户名或密码错误", param));
            } else {
                //登录成功，返回用户信息
                SignInformationModel signModel = SignCommon.SignIn(dbResultMap.keySet().iterator().next(), null, null);
                return formationResult.formationResult(ResponseResultCode.Success, signModel.token, new ExecuteResultParam(dbResultMap, false));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("SignOut")
    public String SignOut(String param) {

        try {
            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, null);
            smartPayAnalyzeParam.getToken();
            SignCommon.SignOut(smartPayAnalyzeParam.getToken());
            return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam());

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            UtileSmart.FreeObjects(param);
        }
    }

    @POST
    @Path("ModifyUserPassword")
    public String ModifyUserPassword(String param) {

        String paramKey_Password = "Password";
        String paramKey_OldPassword = "OldPassword";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;

        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_OldPassword, null);
            paramMap.put(paramKey_Password, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            sqlStr = String.format("SELECT UserId FROM UserDetail where UserId='%s' and Password='%s' ", signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_OldPassword));

            //判断token，并获取对应的用户信息
            //判断旧密码是否争取，
            //旧密码正确，修改密码
            //旧密码错误，提示密码错误
            sqlStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (sqlStr == null || sqlStr.isEmpty()) {
                return formationResult.formationResult(ResponseResultCode.ErrorUserOrPwd, new ExecuteResultParam("会话无效", param));
            }
            sqlStr = String.format("update UserDetail set Password='%s'  where UserId='%s' and Password='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_Password), signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_OldPassword));
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("FindMyPassword")
    public String FindMyPassword(String param) {

        String paramKey_Password = "Password";
        String paramKey_UserName = "UserName";
        String paramKey_VerifyCode = "VerifyCode";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_UserName, null);
            paramMap.put(paramKey_Password, null);
            paramMap.put(paramKey_VerifyCode, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            //验证用户名是否正确
            sqlStr = String.format("select UserId from UserDetail where UserName='%s'", UtileSmart.getStringFromMap(paramMap, paramKey_UserName));
            selectReultStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (selectReultStr == null || selectReultStr.isEmpty()) {
                return formationResult.formationResult(ResponseResultCode.ErrorUserName, new ExecuteResultParam("用户名错误", param));
            }
            //查询用户手机，验证验证码
            sqlStr = String.format("update UserDetail set Password='%s'  where UserName='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_Password), UtileSmart.getStringFromMap(paramMap, paramKey_UserName));
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (resultParam.ResultCode == 1) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }

        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("ModifyVerifyPhone")
    public String ModifyVerifyPhone(String param) {

        String paramKey_Password = "Password";
        String paramKey_VerifyCode = "VerifyCode";
        String paramKey_VerifyPhone = "VerifyPhone";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_Password, null);
            paramMap.put(paramKey_VerifyCode, null);
            paramMap.put(paramKey_VerifyPhone, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            sqlStr = String.format("SELECT UserId FROM UserDetail where UserId='%s' and Password='%s' ", signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_Password));

            //判断token，并获取对应的用户信息
            //判断旧密码是否争取，
            //旧密码正确，修改密码
            //旧密码错误，提示密码错误
            sqlStr = DBHelper.ExecuteSqlSelectOne(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (sqlStr == null || sqlStr.isEmpty()) {
                return formationResult.formationResult(ResponseResultCode.ErrorUserOrPwd, new ExecuteResultParam("会话无效", param));
            }
            sqlStr = String.format("update UserDetail set VerifyPhone='%s'  where UserId='%s' and Password='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_VerifyPhone), signModel.Id, UtileSmart.getStringFromMap(paramMap, paramKey_Password));
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

    @POST
    @Path("ModifyUserDetail")
    public String ModifyUserDetail(String param) {

        String paramKey_RealName = "RealName";
        String paramKey_Gender = "Gender";
        String paramKey_Birthday = "Birthday";
        String paramKey_IdCard = "IdCard";

        ExecuteResultParam resultParam = null;
        String sqlStr = null, selectReultStr = null;
        Map<String, Object> paramMap = null;
        SignInformationModel signModel = null;
        try {
            //get uName 姓名  uPwd 登录密码
            paramMap = new HashMap<String, Object>();

            paramMap.put(paramKey_RealName, null);
            paramMap.put(paramKey_Gender, null);
            paramMap.put(paramKey_Birthday, null);
            paramMap.put(paramKey_IdCard, null);

            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            smartPayAnalyzeParam.AnalyzeParamBodyToMap(param, paramMap);
            signModel = SignCommon.verifySign(smartPayAnalyzeParam.getToken(), false);
            if (signModel == null) {
                return formationResult.formationResult(ResponseResultCode.ErrorSignToken, new ExecuteResultParam("会话无效", param));
            }
            sqlStr = String.format("update UserDetail set RealName='%s' ,Gender= %s , Birthday='%s' , IdCard='%s'  where UserId='%s' ", UtileSmart.getStringFromMap(paramMap, paramKey_RealName), UtileSmart.getStringFromMap(paramMap, paramKey_Gender), UtileSmart.getStringFromMap(paramMap, paramKey_Birthday), UtileSmart.getStringFromMap(paramMap, paramKey_IdCard), signModel.Id);
            resultParam = DBHelper.ExecuteSql(smartPayAnalyzeParam.getRSID(), sqlStr);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(resultParam.errMsg, param));
            }
        } catch (Exception e) {
            return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam(e.getLocalizedMessage(), param, e));
        } finally {
            paramMap.clear();
            UtileSmart.FreeObjects(resultParam, param, sqlStr, paramMap);
        }
    }

}
